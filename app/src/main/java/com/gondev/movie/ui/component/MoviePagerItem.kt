package com.gondev.movie.ui.component

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gondev.domain.model.MovieModel
import com.gondev.movie.makeImgPath
import com.gondev.movie.ui.theme.MovietmdbTheme

@Composable
fun MoviePagerItem(
    modifier: Modifier = Modifier,
    movieModel: MovieModel,
    onClick: () -> Unit
) {
    Log.v("Item", "MoviePagerItem: ${movieModel.title}")
    LaunchedEffect(Unit) {
        Log.d("Item", "create MoviePagerItem: ${movieModel.title}")
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f),
            model = makeImgPath(movieModel.backdropPath),
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
                model = makeImgPath(movieModel.posterPath),
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
                    Alignment.CenterVertically
                )
            ) {
                Text(movieModel.title)
                Text(
                    "${movieModel.releaseDate} ⭐ ${
                        String.format(
                            "%.1f",
                            movieModel.voteAverage
                        )
                    }"
                )
                Text(
                    movieModel.overview,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoviePagerItemPreview() {
    MovietmdbTheme {
        MoviePagerItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            movieModel = MovieModel.createTestInstance()
        ) {}
    }
}