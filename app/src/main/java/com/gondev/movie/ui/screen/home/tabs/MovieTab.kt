package com.gondev.movie.ui.screen.home.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer
import com.gondev.movie.ui.common.ContentsWithNetworkState
import com.gondev.movie.ui.common.ErrorScreen
import com.gondev.movie.ui.common.LoadingScreen
import com.gondev.movie.ui.component.MediaItem
import com.gondev.movie.ui.component.MoviePagerItem
import com.gondev.movie.ui.component.SimpleMediaItem
import com.gondev.movie.ui.theme.MovietmdbTheme
import com.gondev.networkfetcher.NetworkResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieTab(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel = hiltViewModel(),
    gotoDetail: (IMediaModel) -> Unit
) {
    val movieFeed by viewModel.movieFeed.collectAsStateWithLifecycle()
    val movieData = movieFeed.data

    if (movieData != null) {
        MovieTab(
            modifier = modifier,
            isLoading = movieFeed is NetworkResult.Loading,
            isError = movieFeed is NetworkResult.Error,
            onRefresh = { movieFeed.refresh() },
            gotoDetail = gotoDetail,
            nowPlaying = movieData.first,
            upcoming = movieData.second,
            trending = movieData.third
        )
    } else if (movieFeed is NetworkResult.Loading) {
        LoadingScreen(modifier)
    } else {
        ErrorScreen(
            modifier = modifier,
            message = "데이터를 불러오는 데 실패했습니다.",
            onRetryClick = movieFeed::refresh
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieTab(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    gotoDetail: (IMediaModel) -> Unit,
    nowPlaying: PageContainer<MovieModel>,
    upcoming: PageContainer<MovieModel>,
    trending: PageContainer<MovieModel>
) {
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isLoading) {
        if (!isLoading)
            isRefreshing = false
    }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
        },
    ) {
        ContentsWithNetworkState(
            modifier = modifier,
            isLoading = isLoading,
            isError = isError
        ) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    HorizontalPager(
                        state = rememberPagerState(pageCount = { trending.results.size }),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) { index ->
                        val movie = trending.results[index]
                        MoviePagerItem(
                            modifier = Modifier
                                .fillMaxSize(),
                            movieModel = movie,
                            onClick = { gotoDetail(movie) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // 간격 추가

                    // Upcoming Movies 섹션
                    Text(
                        text = "Upcoming Movies",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp), // 좌우 패딩
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // 아이템 간 간격
                    ) {
                        items(upcoming.results) { movie ->
                            SimpleMediaItem(
                                mediaItem = movie,
                                onClick = { gotoDetail(movie) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // 간격 추가

                    // Trending Movies 섹션
                    Text(
                        text = "Now Playing",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                items(nowPlaying.results) { movie ->
                    MediaItem(
                        mediaModel = movie,
                        onClick = { gotoDetail(movie) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieTabPreview() {
    MovietmdbTheme {
        MovieTab(
            isLoading = false,
            isError = false,
            onRefresh = {},
            gotoDetail = {},
            nowPlaying = PageContainer.createTestInstance(
                listOf(
                    MovieModel.createTestInstance(1),
                    MovieModel.createTestInstance(2),
                    MovieModel.createTestInstance(3)
                )
            ),

            upcoming = PageContainer.createTestInstance(
                listOf(
                    MovieModel.createTestInstance(4),
                    MovieModel.createTestInstance(5),
                    MovieModel.createTestInstance(6),
                    MovieModel.createTestInstance(7)
                )
            ),

            trending = PageContainer.createTestInstance(
                listOf(
                    MovieModel.createTestInstance(8),
                    MovieModel.createTestInstance(9),
                    MovieModel.createTestInstance(10),
                    MovieModel.createTestInstance(11)
                )
            )

        )
    }
}