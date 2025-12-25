package com.gondev.networkfetcher

import kotlinx.coroutines.flow.MutableSharedFlow

sealed class NetworkResult<T>(
    private val refreshTrigger: MutableSharedFlow<Unit>,
) {
    abstract val data: T?

    class Loading<T>(
        override val data: T? = null,
        refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(1)
    ) : NetworkResult<T>(refreshTrigger)

    class Success<T>(
        override val data: T,
        refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(1)
    ) : NetworkResult<T>(refreshTrigger)

    class Error<T>(
        val exception: Exception,
        override val data: T? = null,
        refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(1)
    ) : NetworkResult<T>(refreshTrigger) {
        override fun hasException(block: (Exception) -> Unit) {
            block(exception)
        }
    }

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }

    fun copy(data: T?): NetworkResult<T> {
        return when (this) {
            is Loading -> Loading(data, refreshTrigger)
            is Success -> Success(data!!, refreshTrigger)
            is Error -> Error(exception, data, refreshTrigger)
        }
    }

    inline fun <R> hasData(block: (data: T) -> R): R? {
        return data?.let(block)
    }

    open fun hasException(block: (exception: Exception) -> Unit) {}
}