package com.gondev.networkfetcher

import kotlinx.coroutines.flow.MutableSharedFlow

sealed class MutateResult<P, R>(
    private val refreshTrigger: MutableSharedFlow<P>,
) {
    abstract val data: R?

    fun fetch(param: P) {
        refreshTrigger.tryEmit(param)
    }

    data class Idle<P, R>(
        val refreshTrigger: MutableSharedFlow<P>,
        override val data: R? = null
    ) : MutateResult<P, R>(refreshTrigger)

    data class Loading<P, R>(
        val refreshTrigger: MutableSharedFlow<P>,
        override val data: R? = null // 새 데이터를 로드하는 동안 이전 데이터를 보유할 수 있음
    ) : MutateResult<P, R>(refreshTrigger)

    data class Success<P, R>(
        val refreshTrigger: MutableSharedFlow<P>,
        override val data: R // 성공 시에는 반드시 데이터가 있음
    ) : MutateResult<P, R>(refreshTrigger)

    data class Error<P, R>(
        val refreshTrigger: MutableSharedFlow<P>,
        val exception: Throwable, // 에러의 원인 제공
        override val data: R? = null // 에러 시에도 이전 데이터를 보유할 수 있음
    ) : MutateResult<P, R>(refreshTrigger)
}