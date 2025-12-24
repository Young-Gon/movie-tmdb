package com.gondev.movie.ui.screen.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondev.domain.usecase.GetTvFeedUseCase
import com.gondev.networkfetcher.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TVViewModel @Inject constructor(
    private val getTVFeedUseCase: GetTvFeedUseCase
) : ViewModel() {

    val tvFeed = getTVFeedUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NetworkResult.Loading()
    )
}