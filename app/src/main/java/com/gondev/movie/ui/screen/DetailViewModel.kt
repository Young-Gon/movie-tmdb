package com.gondev.movie.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondev.domain.model.IMediaModel
import com.gondev.domain.usecase.DetailUseCase
import com.gondev.networkfetcher.NetworkResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
class DetailViewModel @AssistedInject constructor(
    @Assisted initialMediaModel: IMediaModel,
    detailUseCase: DetailUseCase
) : ViewModel() {

    val detail = detailUseCase(initialMediaModel).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NetworkResult.Loading()
    )

    @AssistedFactory
    interface Factory {
        fun create(initialMediaModel: IMediaModel): DetailViewModel
    }
}
