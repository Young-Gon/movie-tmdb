package com.gondev.movie.ui.screen.home.tabs

import app.cash.turbine.test
import com.gondev.domain.model.MediaType
import com.gondev.domain.model.PageContainer
import com.gondev.domain.usecase.SearchMediaUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val searchMediaUseCase: SearchMediaUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // ViewModel 내의 viewModelScope가 테스트 디스패처를 사용하도록 설정
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(searchMediaUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `검색_의도를_처리하면_로딩_상태가_되고_결과를_가져와야_한다`() = runTest {
        // Given
        val query = "Avengers"
        val mediaType = MediaType.MOVIE
        val mockResults =
            PageContainer.createTestInstance(emptyList<com.gondev.domain.model.IMediaModel>())

        coEvery { searchMediaUseCase(query, mediaType) } returns mockResults

        viewModel.state.test {
            // Initial state
            assertEquals(SearchState(), awaitItem())

            // When
            viewModel.handleIntent(SearchIntent.Search(query))

            // Then - 로딩 중 상태 확인
            assertTrue(awaitItem().isLoading)

            // Then - 결과 확인
            assertEquals(mockResults, awaitItem().copy(isLoading = false).results)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `검색_중_에러가_발생하면_에러_상태와_SideEffect가_방출되어야_한다`() = runTest {
        // Given
        val query = "ErrorQuery"
        val exception = RuntimeException("Network Error")

        coEvery { searchMediaUseCase(any(), any()) } throws exception

        viewModel.sideEffect.test {
            // When
            viewModel.handleIntent(SearchIntent.Search(query))

            // Then - SideEffect 확인
            val sideEffect = awaitItem()
            assertTrue(sideEffect is SearchSideEffect.ShowError)
            assertEquals(exception, (sideEffect as SearchSideEffect.ShowError).error)

            // Then - State 확인
            advanceUntilIdle()
            assertFalse(viewModel.state.value.isLoading)
            assertEquals(exception, viewModel.state.value.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `미디어_타입을_변경하면_새로운_타입으로_검색을_수행해야_한다`() = runTest {
        // Given
        val query = "" // 초기 쿼리
        val newMediaType = MediaType.TV

        coEvery {
            searchMediaUseCase(
                query,
                newMediaType
            )
        } returns PageContainer.createTestInstance(emptyList())

        viewModel.state.test {
            var testState = SearchState()
            assertEquals(testState, awaitItem())
            // When
            viewModel.handleIntent(SearchIntent.SetMediaType(newMediaType))
            // Then - 로딩 중 상태 확인
            var result = awaitItem()
            testState = testState.copy(isLoading = true, mediaType = newMediaType)
            assertEquals(testState, result)

            // Then - 결과 확인
            result = awaitItem()
            testState = testState.copy(isLoading = false, results = PageContainer.createTestInstance(emptyList()))
            assertEquals(testState, result)
        }
    }
}
