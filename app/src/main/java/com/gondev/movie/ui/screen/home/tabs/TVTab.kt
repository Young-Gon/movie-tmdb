package com.gondev.movie.ui.screen.home.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.gondev.domain.model.PageContainer
import com.gondev.domain.model.TVModel
import com.gondev.movie.ui.common.ContentsWithNetworkState
import com.gondev.movie.ui.common.ErrorScreen
import com.gondev.movie.ui.common.LoadingScreen
import com.gondev.movie.ui.component.SimpleMediaItem
import com.gondev.movie.ui.theme.MovietmdbTheme
import com.gondev.networkfetcher.NetworkResult

@Composable
fun TVTab(
    modifier: Modifier = Modifier,
    viewModel: TVViewModel = hiltViewModel(),
    gotoDetail: (IMediaModel) -> Unit
) {
    val tvFeed by viewModel.tvFeed.collectAsStateWithLifecycle()
    val tvData = tvFeed.data

    if (tvData != null) {
        TvTab(
            modifier = modifier,
            isLoading = tvFeed is NetworkResult.Loading,
            isError = tvFeed is NetworkResult.Error,
            onRefresh = { tvFeed.refresh() },
            gotoDetail = gotoDetail,
            airingToday = tvData.first,
            topRateTv = tvData.second,
            trending = tvData.third
        )
    } else if (tvFeed is NetworkResult.Loading) {
        LoadingScreen(modifier)
    } else {
        ErrorScreen(
            modifier = modifier,
            message = "데이터를 불러오는 데 실패했습니다.",
            onRetryClick = tvFeed::refresh
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TvTab(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    gotoDetail: (IMediaModel) -> Unit,
    airingToday: PageContainer<TVModel>,
    topRateTv: PageContainer<TVModel>,
    trending: PageContainer<TVModel>
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                // Upcoming Movies 섹션
                Text(
                    text = "Airing Today",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp), // 좌우 패딩
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // 아이템 간 간격
                ) {
                    items(airingToday.results) { tVModel ->
                        SimpleMediaItem(
                            mediaItem = tVModel,
                            onClick = { gotoDetail(tVModel) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                // Upcoming Movies 섹션
                Text(
                    text = "Top Rate TV",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp), // 좌우 패딩
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // 아이템 간 간격
                ) {
                    items(topRateTv.results) { tVModel ->
                        SimpleMediaItem(
                            mediaItem = tVModel,
                            onClick = { gotoDetail(tVModel) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                // Upcoming Movies 섹션
                Text(
                    text = "Trending",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp), // 좌우 패딩
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // 아이템 간 간격
                ) {
                    items(trending.results) { tVModel ->
                        SimpleMediaItem(
                            mediaItem = tVModel,
                            onClick = { gotoDetail(tVModel) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TvTabPreview() {
    MovietmdbTheme {
        TvTab(
            modifier = Modifier.fillMaxSize(),
            isLoading = false,
            isError = false,
            onRefresh = {},
            gotoDetail = {},
            airingToday = PageContainer.createTestInstance(
                listOf(
                    TVModel.createTestInstance(1),
                    TVModel.createTestInstance(2),
                    TVModel.createTestInstance(3),
                    TVModel.createTestInstance(10),
                )
            ),
            topRateTv = PageContainer.createTestInstance(
                listOf(
                    TVModel.createTestInstance(4),
                    TVModel.createTestInstance(5),
                    TVModel.createTestInstance(6),
                    TVModel.createTestInstance(11),
                )
            ),
            trending = PageContainer.createTestInstance(
                listOf(
                    TVModel.createTestInstance(7),
                    TVModel.createTestInstance(8),
                    TVModel.createTestInstance(9),
                    TVModel.createTestInstance(12),
                )
            ),
        )
    }
}