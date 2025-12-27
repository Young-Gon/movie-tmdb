package com.gondev.movie.ui.screen.home.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer
import com.gondev.domain.model.TVModel
import com.gondev.movie.ui.common.ErrorScreen
import com.gondev.movie.ui.common.LoadingScreen
import com.gondev.movie.ui.common.dialog.MovieDialog
import com.gondev.movie.ui.common.dialog.MovieDialogButton
import com.gondev.movie.ui.component.MediaItem
import com.gondev.movie.ui.theme.MovietmdbTheme
import com.gondev.networkfetcher.MutateResult
import kotlinx.coroutines.launch

@Composable
fun SearchTab(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    gotoDetail: (IMediaModel) -> Unit
) {
    val searchState by viewModel.search.collectAsStateWithLifecycle()

    SearchTab(
        modifier = modifier,
        searchState = searchState,
        gotoDetail = gotoDetail
    )
}

@Composable
private fun SearchTab(
    modifier: Modifier,
    searchState: MutateResult<String, Pair<PageContainer<MovieModel>, PageContainer<TVModel>>>,
    gotoDetail: (IMediaModel) -> Unit
) {
    Log.v("tab", "search")
    LaunchedEffect(Unit) {
        Log.d("tab", "create search")
    }
    val (keyword, setKeyword) = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    fun onSearch() {
        if ( keyword.isBlank()) {
            MovieDialog.showDialog(
                title = "검색어를 입력해 주세요",
                onOk = MovieDialogButton.Ok {}
            )
            return
        }
        searchState.fetch(keyword, onError = { _, _ ->
            MovieDialog.showDialog(
                title = "네트워크 오류",
                body = "네트워크 상태를 확인 하세요",
                onOk = MovieDialogButton.Ok("확인") {}
            )
        })
        keyboardController?.hide()
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.surface,
            ),
            shape = CircleShape,
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text("Search for Movie or TV Show")
            },
            value = keyword,
            trailingIcon = {
                IconButton(
                    enabled = searchState !is MutateResult.Loading,
                    onClick = ::onSearch
                ) {
                    Icon(Icons.Default.Search, "search")
                }
            },
            onValueChange = setKeyword,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            )
        )

        when (searchState) {
            is MutateResult.Idle -> { /* DO NOTHING */
            }

            is MutateResult.Loading -> LoadingScreen()
            is MutateResult.Error -> ErrorScreen("데이터를 불러오는 데 실패했습니다.") {
                searchState.fetch(keyword)
            }

            is MutateResult.Success -> {
                val (movies, tvShows) = searchState.data
                val pagerState = rememberPagerState { 2 }
                val coroutineScope = rememberCoroutineScope()

                // 각 탭의 스크롤 위치를 기억하기 위한 상태 추가
                val movieLazyListState = rememberLazyListState()
                val tvLazyListState = rememberLazyListState()

                Column(modifier = Modifier.fillMaxSize()) {
                    SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
                        Tab(
                            selected = pagerState.currentPage == 0,
                            onClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(0) }
                            },
                            text = { Text("Movie") }
                        )
                        Tab(
                            selected = pagerState.currentPage == 1,
                            onClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(1) }
                            },
                            text = { Text("TV") }
                        )
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        val list = if (page == 0) movies.results else tvShows.results
                        val lazyListState = if (page == 0) movieLazyListState else tvLazyListState

                        if (list.isEmpty())
                            ErrorScreen("검색 결과가 없습니다.")
                        else
                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(vertical = 16.dp)
                            ) {
                                items(list, key = { it.id }) { item ->
                                    MediaItem(
                                        mediaModel = item,
                                        onClick = gotoDetail
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}

class SearchTabPreviewParameterProvider :
    CollectionPreviewParameterProvider<MutateResult<String, Pair<PageContainer<MovieModel>, PageContainer<TVModel>>>>(
        listOf(
            MutateResult.Idle(),
            MutateResult.Loading(),
            MutateResult.Error(
                exception = Exception("네트워크 연결 실패"),
            ),
            MutateResult.Success(
                data = PageContainer.createTestInstance(
                    listOf(
                        MovieModel.createTestInstance(1),
                        MovieModel.createTestInstance(2)
                    )
                ) to PageContainer.createTestInstance(
                    listOf(
                        TVModel.createTestInstance(1),
                        TVModel.createTestInstance(2)
                    )
                )
            )
        )
    )

@Preview(showBackground = true, name = "1. 검색 전 (Idle)")
@Composable
private fun SearchTabIdlePreview(
    @PreviewParameter(SearchTabPreviewParameterProvider::class) searchState: MutateResult<String, Pair<PageContainer<MovieModel>, PageContainer<TVModel>>>
) {
    MovietmdbTheme {
        SearchTab(
            modifier = Modifier.fillMaxSize(),
            searchState = searchState,
            gotoDetail = {}
        )
    }
}
