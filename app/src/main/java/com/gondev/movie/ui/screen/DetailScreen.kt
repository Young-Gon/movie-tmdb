package com.gondev.movie.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.IMedialDetail
import com.gondev.domain.model.IMovieModel
import com.gondev.domain.model.MovieDetailModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.TVDetailModel
import com.gondev.movie.makeImgPath
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
    val uriHandler = LocalUriHandler.current

    detail.hasData { detail ->
        Column(modifier.fillMaxSize()) {
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
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.6f),
                    model = makeImgPath(detail.backdropPath),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "background",
                    placeholder = ColorPainter(Color.Gray),
                    error = ColorPainter(Color.Gray)
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(8.dp)),
                        model = makeImgPath(detail.posterPath),
                        contentDescription = "poster",
                        placeholder = ColorPainter(Color.Gray),
                        error = ColorPainter(Color.Gray)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.Bottom
                        )
                    ) {
                        Text(
                            detail.title,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            if (detail !is IMedialDetail || detail.productionCompanies.isEmpty())
                                ""
                            else
                                detail.productionCompanies[0].name + if (detail.productionCompanies.size > 1) {
                                    " 외 ${detail.productionCompanies.size - 1}개"
                                } else {
                                    ""
                                }
                        )
                        Text(
                            if (detail !is IMedialDetail || detail.genres.isEmpty())
                                ""
                            else
                                detail.genres.joinToString(", ") { it.name }
                        )
                        Text(
                            "${detail.releaseDate} ⭐ ${
                                if (detail is MovieModel)
                                    String.format(
                                        " ⭐ %.1f",
                                        detail.voteAverage
                                    )
                                else ""
                            }"
                        )
                    }
                }
            }
            if (detail is IMedialDetail) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("시놉시스", fontSize = 16.sp)
                    Text(
                        detail.overview,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    for (video in detail.videos) {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable{
                                uriHandler.openUri("https://www.youtube.com/watch?v=${video.key}")
                            },
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            Icon(Icons.Filled.PlayCircle, "Video")
                            Text(video.name)
                        }
                    }
                }
            }
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

