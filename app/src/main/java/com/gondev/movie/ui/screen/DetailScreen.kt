package com.gondev.movie.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.IMovieModel
import com.gondev.domain.model.MovieDetailModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.TVDetailModel
import com.gondev.movie.ui.theme.MovietmdbTheme
import com.gondev.networkfetcher.NetworkResult

@Composable
fun DetailScreen(modifier: Modifier = Modifier, viewModel: DetailViewModel, onBack: () -> Unit) {
    val detail by viewModel.detail.collectAsStateWithLifecycle()
    DetailScreen(modifier, detail, onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detail: NetworkResult<out IMediaModel>,
    onBack: () -> Unit,
) {

    detail.hasData { detail ->
        Column {
            TopAppBar(
                title = { Text(if (detail is IMovieModel) "Movie" else "TV") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    // 이 화면에만 특화된 저장 버튼 등을 쉽게 추가
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "케시 데이터로 화면 구현")
@Composable
private fun DetailScreenPreview() {
    MovietmdbTheme {
        DetailScreen(
            detail = NetworkResult.Success(MovieModel.createTestInstance(1)),
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "로딩 완료된 영화 정보로 화면 구현")
@Composable
private fun DetailScreenPreview2() {
    MovietmdbTheme {
        DetailScreen(
            detail = NetworkResult.Success(MovieDetailModel.createTestInstance(2)),
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "로딩 완료된 TV 정보로 화면 구현")
@Composable
private fun DetailScreenPreview3() {
    MovietmdbTheme {
        DetailScreen(
            detail = NetworkResult.Success(TVDetailModel.createTestInstance(2)),
            onBack = {}
        )
    }
}

