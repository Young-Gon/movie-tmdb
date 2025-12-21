package com.gondev.networkfetcher

import kotlinx.coroutines.flow.MutableSharedFlow

sealed class NetworkResult<T>(
    private val refreshTrigger: MutableSharedFlow<Unit>,
) {
    abstract val data: T?

    class Loading<T>(
        refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(1),
        override val data: T? = null
    ) : NetworkResult<T>(refreshTrigger)

    class Success<T>(
        refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(1),
        override val data: T
    ) : NetworkResult<T>(refreshTrigger) {
        override fun <R> hasData(block: (data: T) -> R): R {
            return block(data)
        }
    }

    class Error<T>(
        refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(1),
        val exception: Exception,
        override val data: T? = null
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
            is Loading -> Loading(refreshTrigger, data)
            is Success -> Success(refreshTrigger, data!!)
            is Error -> Error(refreshTrigger, exception, data)
        }
    }

    open fun <R> hasData(block: (data: T) -> R): R? {
        return data?.let(block)
    }

    open fun hasException(block: (exception: Exception) -> Unit) {}
}