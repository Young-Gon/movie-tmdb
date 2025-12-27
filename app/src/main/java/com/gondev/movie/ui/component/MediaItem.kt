package com.gondev.movie.ui.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.MovieModel
import com.gondev.movie.makeImgPath
import com.gondev.movie.ui.theme.MovietmdbTheme

@Composable
fun MediaItem(
    modifier: Modifier = Modifier,
    mediaModel: IMediaModel,
    onClick: (IMediaModel) -> Unit
) {
    Log.v("Item", "mediaModel: ${mediaModel.title}")
    LaunchedEffect(Unit) {
        Log.d("Item", "create mediaItem: ${mediaModel.title}")
    }
    val posterPath = remember(mediaModel.posterPath) { makeImgPath(mediaModel.posterPath) }
    val onMediaClick = remember(mediaModel, onClick) { { onClick(mediaModel) } }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onMediaClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .height(120.dp)
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp)),
            model = posterPath,
            contentDescription = "poster",
            placeholder = ColorPainter(Color.Gray),
            error = ColorPainter(Color.Gray)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterVertically
            )
        ) {
            Text(
                mediaModel.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${mediaModel.releaseDate}${
                    if (mediaModel is MovieModel)
                        String.format(
                            " ⭐ %.1f",
                            mediaModel.voteAverage
                        )
                    else ""
                }"
            )
            Text(
                mediaModel.overview,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaItemPreview() {
    MovietmdbTheme {
        MediaItem(
            mediaModel = MovieModel.createTestInstance(),
            onClick = { /* Do nothing for preview */ }
        )
    }
}
