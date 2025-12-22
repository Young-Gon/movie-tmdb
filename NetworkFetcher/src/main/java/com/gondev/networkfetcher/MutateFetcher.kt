package com.gondev.networkfetcher

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest

// P: 요청 파라미터 타입, R: 응답 데이터 타입
class MutateFetcher<P, R>(
    private var cachedData: R? = null,
    private val api: suspend FlowCollector<MutateResult<P, R>>.(P) -> R,
) {
    // mutation을 위한 파라미터를 방출할 Flow
    private val _mutationTrigger = MutableSharedFlow<P>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = _mutationTrigger.transformLatest { params ->
        emit(MutateResult.Loading(_mutationTrigger, cachedData))

        try {
            cachedData = api(params)
            emit(MutateResult.Success(_mutationTrigger, cachedData))
        } catch (e: Exception) {
            emit(MutateResult.Error(_mutationTrigger, e, cachedData))
        }
    }.onStart {
        emit(
            if (cachedData != null)
                MutateResult.Success(_mutationTrigger, cachedData)
            else
                MutateResult.Idle(_mutationTrigger)
        )
    }
}
