package com.gondev.movie.ui.screen.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondev.domain.usecase.SearchMediaUseCase
import com.gondev.networkfetcher.MutateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchMediaUseCase: SearchMediaUseCase
) : ViewModel() {
    val search = searchMediaUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MutateResult.Idle()
    )
}