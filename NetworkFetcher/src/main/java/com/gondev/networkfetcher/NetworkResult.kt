package com.gondev.networkfetcher

sealed class NetworkResult<T>(
    private val onRefresh: (() -> Unit)? = null,
) {
    abstract val data: T?

    class Loading<T>(
        override val data: T? = null,
        onRefresh: (() -> Unit)? = null
    ) : NetworkResult<T>(onRefresh)

    class Success<T>(
        override val data: T,
        onRefresh: (() -> Unit)? = null
    ) : NetworkResult<T>(onRefresh)

    class Error<T>(
        val exception: Exception,
        override val data: T? = null,
        onRefresh: (() -> Unit)? = null
    ) : NetworkResult<T>(onRefresh) {
        override fun hasException(block: (Exception) -> Unit) {
            block(exception)
        }
    }

    fun refresh() {
        onRefresh?.invoke()
    }

    fun copy(data: T?): NetworkResult<T> {
        return when (this) {
            is Loading -> Loading(data, onRefresh)
            is Success -> Success(data!!, onRefresh)
            is Error -> Error(exception, data, onRefresh)
        }
    }

    inline fun <R> hasData(block: (data: T) -> R): R? {
        return data?.let(block)
    }

    open fun hasException(block: (exception: Exception) -> Unit) {}
}
