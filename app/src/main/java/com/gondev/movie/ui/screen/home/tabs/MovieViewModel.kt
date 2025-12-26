package com.gondev.movie.ui.screen.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondev.domain.usecase.GetMovieFeedUseCase
import com.gondev.networkfetcher.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    getMovieFeedUseCase: GetMovieFeedUseCase
) : ViewModel() {

    val movieFeed = getMovieFeedUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = NetworkResult.Loading()
    )
}