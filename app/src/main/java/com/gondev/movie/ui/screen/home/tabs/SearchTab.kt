package com.gondev.movie.ui.screen.home.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.gondev.domain.model.MediaType
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer
import com.gondev.movie.ui.common.ErrorScreen
import com.gondev.movie.ui.common.LoadingScreen
import com.gondev.movie.ui.common.dialog.MovieDialog
import com.gondev.movie.ui.common.dialog.MovieDialogButton
import com.gondev.movie.ui.component.MediaItem
import com.gondev.movie.ui.theme.MovietmdbTheme

@Composable
fun SearchTab(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    gotoDetail: (IMediaModel) -> Unit
) {
    val searchState by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SearchSideEffect.ShowError -> {
                    MovieDialog.showDialog(
                        icon = Icons.Default.Search,
                        title = "검색 실패",
                        body = sideEffect.error.message,
                        onOk = MovieDialogButton.Ok {}
                    )
                }

                is SearchSideEffect.ShowToast -> TODO()
            }
        }
    }

    SearchTab(
        modifier = modifier,
        searchState = searchState,
        onIntent = viewModel::handleIntent,
        gotoDetail = gotoDetail
    )
}

@Composable
private fun SearchTab(
    modifier: Modifier,
    searchState: SearchState,
    onIntent: (SearchIntent) -> Unit,
    gotoDetail: (IMediaModel) -> Unit
) {
    Log.v("tab", "search")
    LaunchedEffect(Unit) {
        Log.d("tab", "create search")
    }

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(isLoading = searchState.isLoading, searchIn = searchState.mediaType, onIntent = onIntent)

        if (searchState.error != null)
            ErrorScreen("데이터를 불러오는 데 실패했습니다.")
        else if (searchState.isLoading)
            LoadingScreen()
        else if (searchState.results != null) {
            val list = searchState.results.results
            if (list.isEmpty())
                ErrorScreen("검색 결과가 없습니다.")
            else
                SearchResultList(list, gotoDetail)
        }
    }
}

@Composable
private fun SearchBar(
    isLoading: Boolean,
    searchIn: MediaType,
    onIntent: (SearchIntent) -> Unit,
) {
    val (keyword, setKeyword) = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }

    fun onSearch() {
        if (keyword.isBlank()) {
            MovieDialog.showDialog(
                title = "검색어를 입력해 주세요",
                onOk = MovieDialogButton.Ok {}
            )
            return
        }
        onIntent(SearchIntent.Search(keyword))
        keyboardController?.hide()
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = CircleShape,
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text("Search")
        },
        value = keyword,
        leadingIcon = {
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(searchIn.name)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    MediaType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                onIntent(SearchIntent.SetMediaType(type))
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        trailingIcon = {
            IconButton(
                enabled = !(keyword.isBlank() || isLoading),
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

@Composable
private fun SearchResultList(
    list: List<IMediaModel>,
    gotoDetail: (IMediaModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // results.results가 List<out IMediaModel> 이므로
        // items에서 key를 사용할 때 ID를 안전하게 참조할 수 있습니다.
        items(list, key = { it.id }) { item ->
            MediaItem(
                mediaModel = item,
                onClick = gotoDetail
            )
        }
    }
}

class SearchTabPreviewParameterProvider :
    CollectionPreviewParameterProvider<SearchState>(
        listOf(
            SearchState(), // Idle
            SearchState(isLoading = true), // Loading
            SearchState(error = Throwable("Network error")), // Error
            SearchState(
                results = PageContainer.createTestInstance(
                    listOf(
                        MovieModel.createTestInstance(1),
                        MovieModel.createTestInstance(2),
                        MovieModel.createTestInstance(3)
                    )
                )
            ), // Success with results
            SearchState(
                results = PageContainer.createTestInstance(emptyList())
            ) // Success with no results
        )
    )

@Preview(showBackground = true, name = "SearchTab States")
@Composable
private fun SearchTabPreview(
    @PreviewParameter(SearchTabPreviewParameterProvider::class) searchState: SearchState
) {
    MovietmdbTheme {
        SearchTab(
            modifier = Modifier.fillMaxSize(),
            searchState = searchState,
            onIntent = {},
            gotoDetail = {}
        )
    }
}
