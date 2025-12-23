package com.gondev.movie.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.gondev.movie.ui.theme.MovietmdbTheme

@Composable
fun ContentsWithNetworkState(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isError: Boolean,
    onRetryClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        content()
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (isError) {
            Surface(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "데이터를 불러오는 데 실패했습니다.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    InputChip(
                        selected = false,
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = MaterialTheme.colorScheme.error,
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        onClick = onRetryClick ?: {},
                        label = {
                            Text(
                                text = "재시도",
                                color = MaterialTheme.colorScheme.onError,
                            )
                        }
                    )
                }
            }
        }
    }
}

class ContentsWithNetworkStatePreviewParameter :
    CollectionPreviewParameterProvider<Pair<Boolean, Boolean>>(
        listOf(
            true to false,
            false to true,
            false to false
        )
    )

@Preview
@Composable
private fun ContentsWithNetworkStatePreview(
    @PreviewParameter(ContentsWithNetworkStatePreviewParameter::class)
    isLoadingAndError: Pair<Boolean, Boolean>
) {
    MovietmdbTheme {
        ContentsWithNetworkState(
            isLoading = isLoadingAndError.first,
            isError = isLoadingAndError.second
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(("Contents"))
            }
        }
    }
}