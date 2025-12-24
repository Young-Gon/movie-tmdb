package com.gondev.movie.ui.screen.home.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.gondev.movie.ui.theme.MovietmdbTheme
import com.gondev.networkfetcher.MutateResult

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
    val (keyword, setKeyword) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    fun onSearch() {
        searchState.fetch(keyword,){

        }
        keyboardController?.hide()
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
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