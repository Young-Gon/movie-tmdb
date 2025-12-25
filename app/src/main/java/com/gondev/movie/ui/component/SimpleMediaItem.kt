package com.gondev.movie.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun SimpleMediaItem(
    modifier: Modifier = Modifier,
    mediaItem: IMediaModel,
    onClick: (IMediaModel) -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(100.dp)
            .clickable(onClick = { onClick(mediaItem) }),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp)),
            model = makeImgPath(mediaItem.posterPath),
            contentDescription = "poster",
            placeholder = ColorPainter(Color.Gray),
            error = ColorPainter(Color.Gray)
        )
        Text(
            text = mediaItem.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth() // AsyncImage의 너비만큼 Text가 확장
        )
        Text(mediaItem.releaseDate)
    }
}

@Preview
@Composable
private fun SimpleMediaItemPreview() {
    MovietmdbTheme() {
        SimpleMediaItem(mediaItem = MovieModel.createTestInstance())
    }
}