package com.gondev.networkfetcher

import kotlinx.coroutines.flow.MutableSharedFlow


data class MutateIntent<P, R>(
    val param: P,
    val onError: (suspend (R?, Throwable) -> Unit)? = null,
    val onSuccess: (suspend (R) -> Unit)? = null,
)

sealed class MutateResult<P, R>(
    private val refreshTrigger: MutableSharedFlow<MutateIntent<P, R>>,
) {
    abstract val data: R?

    fun fetch(
        param: P,
        onError: (suspend (R?, Throwable) -> Unit)? = null,
        onSuccess: (suspend (R) -> Unit)? = null
    ) {
        refreshTrigger.tryEmit(MutateIntent(param, onError, onSuccess))
    }

    data class Idle<P, R>(
        val refreshTrigger: MutableSharedFlow<MutateIntent<P, R>> = MutableSharedFlow(),
    ) : MutateResult<P, R>(refreshTrigger) {
        override val data: R? = null
    }

    data class Loading<P, R>(
        val refreshTrigger: MutableSharedFlow<MutateIntent<P, R>> = MutableSharedFlow(),
        override val data: R? = null // 새 데이터를 로드하는 동안 이전 데이터를 보유할 수 있음
    ) : MutateResult<P, R>(refreshTrigger)

    data class Success<P, R>(
        val refreshTrigger: MutableSharedFlow<MutateIntent<P, R>> = MutableSharedFlow(),
        override val data: R // 성공 시에는 반드시 데이터가 있음
    ) : MutateResult<P, R>(refreshTrigger)

    data class Error<P, R>(
        val refreshTrigger: MutableSharedFlow<MutateIntent<P, R>> = MutableSharedFlow(),
        val exception: Throwable, // 에러의 원인 제공
        override val data: R? = null // 에러 시에도 이전 데이터를 보유할 수 있음
    ) : MutateResult<P, R>(refreshTrigger)
}