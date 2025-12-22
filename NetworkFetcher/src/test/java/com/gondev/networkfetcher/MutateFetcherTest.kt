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
    fun `Rapid consecutive successful mutations`() = runTest {
        // Trigger multiple mutations in quick succession. Verify that each mutation goes through the Loading -> Success cycle
        // and the final state correctly reflects the result of the last mutation.
        val fetcher = MutateFetcher<String, String>(
            api = { param ->
                delay(50) // small delay
                "Result for $param"
            }
        )

        fetcher.flow.test {
            val idle = awaitItem()
            assertTrue(idle is MutateResult.Idle)

            // --- First Mutation ---
            idle.fetch("first")

            var loading = awaitItem()
            assertTrue(loading is MutateResult.Loading)
            assertEquals(null, loading.data)

            var success = awaitItem()
            assertTrue(success is MutateResult.Success)
            assertEquals("Result for first", success.data)

            // --- Second Mutation ---
            success.fetch("second")

            loading = awaitItem()
            assertTrue(loading is MutateResult.Loading)
            assertEquals("Result for first", loading.data) // Loading should have old data

            success = awaitItem()
            assertTrue(success is MutateResult.Success)
            assertEquals("Result for second", success.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Mutation success followed by a failure`() = runTest {
        // After a successful mutation that populates the cache, trigger another mutation that fails.
        // Verify the final state is MutateResult.Error, but it still holds the data from the last successful call.
        val initialApiResult = "Initial Success Data"
        val exception = RuntimeException("Subsequent Failure")

        val fetcher = MutateFetcher<Boolean, String>(
            api = { isSuccess ->
                delay(50)
                if (isSuccess) initialApiResult else throw exception
            }
        )

        fetcher.flow.test {
            val idle = awaitItem()
            assertTrue(idle is MutateResult.Idle)

            // First: successful mutation
            idle.fetch(true)
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success1 = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals(initialApiResult, success1.data)

            // Second: failing mutation
            success1.fetch(false) // Trigger failure
            awaitItem().also {
                assertTrue(it is MutateResult.Loading)
                assertEquals(initialApiResult, it.data) // Should hold previous successful data
            }
            val error = awaitItem().also { assertTrue(it is MutateResult.Error) }
            assertEquals(exception, (error as MutateResult.Error).exception)
            assertEquals(initialApiResult, error.data) // Should still hold previous successful data

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Mutation failure followed by a success`() = runTest {
        // After a failed mutation, trigger a new successful mutation.
        // Verify the flow correctly transitions from Error to Loading and then to a final Success state with the new data.
        val initialException = RuntimeException("Initial Failure")
        val subsequentApiResult = "Subsequent Success Data"

        val fetcher = MutateFetcher<Boolean, String>(
            api = { isSuccess ->
                delay(50)
                if (isSuccess) subsequentApiResult else throw initialException
            }
        )

        fetcher.flow.test {
            val idle = awaitItem()
            assertTrue(idle is MutateResult.Idle)

            // First: failing mutation
            idle.fetch(false) // Trigger failure
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val error1 = awaitItem().also { assertTrue(it is MutateResult.Error) }
            assertEquals(initialException, (error1 as MutateResult.Error).exception)
            assertEquals(null, error1.data) // No cached data before this failure

            // Second: successful mutation
            error1.fetch(true) // Trigger success
            awaitItem().also {
                assertTrue(it is MutateResult.Loading)
                assertEquals(null, it.data) // Should hold null as previous was error with null data
            }
            val success2 = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals(subsequentApiResult, success2.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Re subscription to the flow`() = runTest {
        // If a collector cancels and then re-subscribes to the flow after a successful mutation,
        // it should immediately receive a MutateResult.Success with the latest cached data.
        val apiResult = "Successful data after first mutation"
        val fetcher = MutateFetcher<Unit, String>(
            api = {
                delay(50)
                apiResult
            }
        )

        // First subscription and successful mutation
        fetcher.flow.test {
            val idle = awaitItem()
            assertTrue(idle is MutateResult.Idle)

            idle.fetch(Unit)
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals(apiResult, success.data)

            // Cancel the first collector
            cancelAndIgnoreRemainingEvents()
        }

        // Re-subscription: should immediately get the last successful state
        fetcher.flow.test {
            val resubscribedItem = awaitItem()
            assertTrue(resubscribedItem is MutateResult.Success)
            assertEquals(apiResult, resubscribedItem.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Collector cancellation during API call`() = runTest {
        // If the collecting coroutine is cancelled while the API call is suspended, the entire operation should be gracefully cancelled without crashing.
        val fetcher = MutateFetcher<Unit, String>(
            api = {
                delay(1000) // Long delay
                "This should not be returned"
            }
        )

        fetcher.flow.test {
            val idle = awaitItem()
            idle.fetch(Unit)

            // Await the loading state to ensure the API call has started and suspended
            awaitItem().also { assertTrue(it is MutateResult.Loading) }

            // Exiting the 'test' block here will cancel the flow collection.
            // If the cancellation is not handled gracefully inside MutateFetcher,
            // the test will fail with an unhandled exception.
        }

        // Test passes if it completes without crashing.
    }

    @Test
    fun `API call with different parameter types`() = runTest {
        // Test the flow with various types for the parameter 'P', such as data classes, primitives, and lists, to ensure type compatibility.

        // Test with String parameter
        val stringFetcher = MutateFetcher<String, String>(
            api = { param ->
                delay(10)
                "String Result: $param"
            }
        )
        stringFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch("testString")
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals("String Result: testString", success.data)
            cancelAndIgnoreRemainingEvents()
        }

        // Test with Int parameter
        val intFetcher = MutateFetcher<Int, String>(
            api = { param ->
                delay(10)
                "Int Result: $param"
            }
        )
        intFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch(123)
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals("Int Result: 123", success.data)
            cancelAndIgnoreRemainingEvents()
        }

        // Test with List<String> parameter
        val listFetcher = MutateFetcher<List<String>, String>(
            api = { param ->
                delay(10)
                "List Result: ${param.joinToString()}"
            }
        )
        listFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch(listOf("a", "b", "c"))
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals("List Result: a, b, c", success.data)
            cancelAndIgnoreRemainingEvents()
        }

        // Test with a Data Class parameter
        data class MyParam(val id: Int, val name: String)
        val dataClassFetcher = MutateFetcher<MyParam, String>(
            api = { param ->
                delay(10)
                "Data Class Result: ${param.id} - ${param.name}"
            }
        )
        dataClassFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch(MyParam(1, "Test"))
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals("Data Class Result: 1 - Test", success.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `API call with different response types`() = runTest {
        // Test the flow with various types for the response 'R', including nullable types, data classes, and collections, to ensure correct caching and emission.

        // Test with Data Class response
        data class MyResponse(val id: Int, val value: String)
        val dataClassFetcher = MutateFetcher<Unit, MyResponse>(
            api = { MyResponse(1, "Response") }
        )
        dataClassFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch(Unit)
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals(MyResponse(1, "Response"), success.data)
            cancelAndIgnoreRemainingEvents()
        }

        // Test with List<Int> response
        val listFetcher = MutateFetcher<Unit, List<Int>>(
            api = { listOf(1, 2, 3) }
        )
        listFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch(Unit)
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals(listOf(1, 2, 3), success.data)
            cancelAndIgnoreRemainingEvents()
        }

        // Test with nullable but non-null response
        val nullableFetcher = MutateFetcher<Unit, String?>(
            api = { "non-null string" }
        )
        nullableFetcher.flow.test {
            (awaitItem() as MutateResult.Idle).fetch(Unit)
            awaitItem().also { assertTrue(it is MutateResult.Loading) }
            val success = awaitItem().also { assertTrue(it is MutateResult.Success) }
            assertEquals("non-null string", success.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `API call returning Unit response`() = runTest {
        // Test the scenario where the API successfully completes but returns a Unit value for 'R'.
        // The flow should emit MutateResult.Success with Unit data.
        val fetcher = MutateFetcher<String, Unit>(
            api = { param ->
                delay(10)
                println("API call for $param succeeded")
                // Implicitly returns Unit
            }
        )

        fetcher.flow.test {
            val idle = awaitItem()
            assertTrue(idle is MutateResult.Idle)

            idle.fetch("test")

            awaitItem().also { assertTrue(it is MutateResult.Loading) }

            val success = awaitItem()
            assertTrue(success is MutateResult.Success)
            assertEquals(Unit, success.data) // Verify the data is Unit

            cancelAndIgnoreRemainingEvents()
        }
    }

}