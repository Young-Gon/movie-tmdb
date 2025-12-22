package com.gondev.movie.ui.screen.home.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer
import com.gondev.movie.ui.common.ErrorScreen
import com.gondev.movie.ui.common.LoadingScreen
import com.gondev.networkfetcher.NetworkResult

@Composable
fun MovieTab(modifier: Modifier = Modifier, viewModel: MovieViewModel = hiltViewModel()) {
    val movieFeed by viewModel.movieFeed.collectAsStateWithLifecycle()

    when (movieFeed) {
        is NetworkResult.Loading -> {
            LoadingScreen(modifier)
        }

        is NetworkResult.Success -> {
            val successMovieFeed = movieFeed as NetworkResult.Success<Triple<PageContainer<MovieModel>, PageContainer<MovieModel>, PageContainer<MovieModel>>>
            val (nowPlaying, upcoming, trending) = (successMovieFeed ).data

            MovieTab(modifier, nowPlaying, upcoming, trending)
        }

        is NetworkResult.Error -> {
            ErrorScreen(message = "데이터를 불러오는 데 실패했습니다.") {
                movieFeed.refresh()
            }
        }
    }
}

@Composable
private fun MovieTab(
    modifier: Modifier = Modifier,
    nowPlaying: PageContainer<MovieModel>,
    upcoming: PageContainer<MovieModel>,
    trending: PageContainer<MovieModel>
) {
}

@Preview
@Composable
private fun MovieTabPreview() {
    MovieTab()
}