package com.gondev.movie.ui.screen.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.MediaType
import com.gondev.domain.model.PageContainer
import com.gondev.domain.usecase.SearchMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SearchState(
    val isLoading: Boolean = false,
    val mediaType: MediaType = MediaType.MOVIE,
    val query: String = "",
    val results: PageContainer<out IMediaModel>? = null,
    val error: Throwable? = null
)

sealed class SearchIntent {
    data class Search(val query: String) : SearchIntent()
    data class SetMediaType(val mediaType: MediaType) : SearchIntent()
}

sealed class SearchSideEffect {
    data class ShowToast(val message: String) : SearchSideEffect()
    data class ShowError(val error: Throwable) : SearchSideEffect()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMediaUseCase: SearchMediaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SearchSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Search -> search(intent.query)
            is SearchIntent.SetMediaType -> search(mediaType = intent.mediaType)
        }
    }

    private fun search(
        query: String = _state.value.query,
        mediaType: MediaType = _state.value.mediaType
    ) {
        _state.update { it.copy(isLoading = true, query = query, mediaType = mediaType) }
        viewModelScope.launch {
            try {
                val results = searchMediaUseCase(query, mediaType)
                _state.update { it.copy(isLoading = false, results = results, error = null) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e) }
                _sideEffect.emit(SearchSideEffect.ShowError(e))
            }
        }
    }
}