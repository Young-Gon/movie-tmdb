package com.gondev.networkfetcher

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MutateFetcherTest {

    @Test
    fun `Initial state with no cached data`() = runTest {
        // When the flow is first collected and no initial cachedData is provided, it should emit MutateResult.Idle.
        val fetcher = MutateFetcher<Unit, String>(
            api = { "result" }
        )

        fetcher.flow.test {
            val item = awaitItem()
            assertTrue(item is MutateResult.Idle)
            assertEquals(null, item.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial state with pre existing cached data`() = runTest {
        // When the flow is first collected and cachedData is pre-populated, it should emit MutateResult.Success with the cached data.
        val initialData = "initial data"
        val fetcher = MutateFetcher<Unit, String>(
            cachedData = initialData,
            api = { "result" }
        )

        fetcher.flow.test {
            val item = awaitItem()
            assertTrue(item is MutateResult.Success)
            assertEquals(initialData, item.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Successful mutation sequence with no initial cache`() = runTest {
        // When a mutation is triggered via _mutationTrigger, the flow should first emit MutateResult.Loading with null cachedData,
        // then upon successful API call, emit MutateResult.Success with the new data.
        val apiResult = "API Result"
        val fetcher = MutateFetcher<Unit, String>(
            api = {
                delay(100) // Simulate network delay
                apiResult
            }
        )

        fetcher.flow.test {
            // Initial state
            val item = awaitItem()
            assertTrue(item is MutateResult.Idle)
            // Trigger mutation
            item.fetch(Unit)

            // Loading state
            val loadingItem = awaitItem()
            assertTrue(loadingItem is MutateResult.Loading)
            assertEquals(null, loadingItem.data)

            // Success state
            val successItem = awaitItem()
            assertTrue(successItem is MutateResult.Success)
            assertEquals(apiResult, successItem.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Successful mutation sequence with existing cache`() = runTest {
        // When a mutation is triggered with pre-existing cached data, the flow should emit MutateResult.Loading with the old cachedData,
        // then upon successful API call, emit MutateResult.Success with the new data.
        val initialData = "initial data"
        val apiResult = "New API Result"
        val fetcher = MutateFetcher<Unit, String>(
            cachedData = initialData,
            api = {
                delay(100) // Simulate network delay
                apiResult
            }
        )

        fetcher.flow.test {
            // Initial state with cached data
            val initialSuccessItem = awaitItem()
            assertTrue(initialSuccessItem is MutateResult.Success)
            assertEquals(initialData, initialSuccessItem.data)

            // Trigger mutation
            initialSuccessItem.fetch(Unit)

            // Loading state with old cached data
            val loadingItem = awaitItem()
            assertTrue(loadingItem is MutateResult.Loading)
            assertEquals(initialData, loadingItem.data) // Should hold old data during loading

            // Success state with new data
            val successItem = awaitItem()
            assertTrue(successItem is MutateResult.Success)
            assertEquals(apiResult, successItem.data)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `Failed mutation sequence with no initial cache`() = runTest {
        // When a mutation is triggered and the API call throws an exception (with no prior cache), the flow should emit MutateResult.Loading,
        // followed by MutateResult.Error containing the exception and null cachedData.
        val exception = RuntimeException("API Error")
        val fetcher = MutateFetcher<Unit, String>(
            api = {
                delay(100)
                throw exception
            }
        )

        fetcher.flow.test {
            val initial = awaitItem()
            assertTrue(initial is MutateResult.Idle)

            initial.fetch(Unit)

            val loading = awaitItem()
            assertTrue(loading is MutateResult.Loading)
            assertEquals(null, loading.data)

            val error = awaitItem()
            assertTrue(error is MutateResult.Error)
            assertEquals(exception, (error as MutateResult.Error).exception)
            assertEquals(null, error.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Failed mutation sequence with existing cache`() = runTest {
        // When a mutation is triggered and the API call throws an exception (with pre-existing cache), the flow should emit MutateResult.Loading with the old data,
        // followed by MutateResult.Error containing the exception and the old cachedData.
        val initialData = "initial data"
        val exception = RuntimeException("API Error")
        val fetcher = MutateFetcher<Unit, String>(
            cachedData = initialData,
            api = {
                delay(100)
                throw exception
            }
        )

        fetcher.flow.test {
            val initial = awaitItem()
            assertTrue(initial is MutateResult.Success)
            assertEquals(initialData, initial.data)

            initial.fetch(Unit)

            val loading = awaitItem()
            assertTrue(loading is MutateResult.Loading)
            assertEquals(initialData, loading.data)

            val error = awaitItem()
            assertTrue(error is MutateResult.Error)
            assertEquals(exception, (error as MutateResult.Error).exception)
            assertEquals(initialData, error.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Multiple mutations sequence cancellation logic`() = runTest {
        // When a new mutation is triggered while a previous one is in progress (i.e., API call is suspended),
        // the previous operation should be cancelled, and the flow should only proceed with the latest mutation.
        val fetcher = MutateFetcher<String, String>(
            api = { param ->
                delay(500) // Long delay
                "Result for $param"
            }
        )

        fetcher.flow.test {
            val idle = awaitItem()
            assertTrue(idle is MutateResult.Idle)

            // Trigger first mutation
            idle.fetch("first")

            val loading1 = awaitItem()
            assertTrue(loading1 is MutateResult.Loading)

            // Immediately trigger second mutation
            loading1.fetch("second")

            // A new loading state should be emitted for the second call, cancelling the first.
            val loading2 = awaitItem()
            assertTrue(loading2 is MutateResult.Loading)

            // The flow should now only yield the result for "second".
            val success = awaitItem()
            assertTrue(success is MutateResult.Success)
            assertEquals("Result for second", success.data)

            // We can advance time to ensure the first one would have completed, but it was cancelled.
            testScheduler.advanceTimeBy(501)

            // There should be no more events.
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `Rapid consecutive successful mutations`() {
        // Trigger multiple mutations in quick succession. Verify that each mutation goes through the Loading -> Success cycle 
        // and the final state correctly reflects the result of the last mutation.
        // TODO implement test
    }

    @Test
    fun `Mutation success followed by a failure`() {
        // After a successful mutation that populates the cache, trigger another mutation that fails. 
        // Verify the final state is MutateResult.Error, but it still holds the data from the last successful call.
        // TODO implement test
    }

    @Test
    fun `Mutation failure followed by a success`() {
        // After a failed mutation, trigger a new successful mutation. 
        // Verify the flow correctly transitions from Error to Loading and then to a final Success state with the new data.
        // TODO implement test
    }

    @Test
    fun `Re subscription to the flow`() {
        // If a collector cancels and then re-subscribes to the flow after a successful mutation, 
        // it should immediately receive a MutateResult.Success with the latest cached data.
        // TODO implement test
    }

    @Test
    fun `Collector cancellation during API call`() {
        // If the collecting coroutine is cancelled while the API call is suspended, the entire operation should be gracefully cancelled without crashing.
        // TODO implement test
    }

    @Test
    fun `API call with different parameter types`() {
        // Test the flow with various types for the parameter 'P', such as data classes, primitives, and lists, to ensure type compatibility.
        // TODO implement test
    }

    @Test
    fun `API call with different response types`() {
        // Test the flow with various types for the response 'R', including nullable types, data classes, and collections, to ensure correct caching and emission.
        // TODO implement test
    }

    @Test
    fun `API call returning null response`() {
        // Test the scenario where the API successfully completes but returns a null value for 'R'. The cachedData should be updated to null, 
        // and the flow should emit MutateResult.Success with null data.
        // TODO implement test
    }

}