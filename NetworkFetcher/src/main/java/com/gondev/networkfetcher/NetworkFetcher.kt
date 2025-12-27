package com.gondev.networkfetcher

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.transformLatest

class NetworkFetcher<R>(
    private var cachedData: R? = null,
    private val api: suspend FlowCollector<NetworkResult<R>>.() -> R,
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    private var isLoaded = false

    /*val flow = channelFlow {
        launch {
            refreshTrigger.collectLatest {
                send(NetworkResult.Loading(refreshTrigger, cachedData))
                try {
                     cachedData = api()
                    send(NetworkResult.Success(refreshTrigger, cachedData))
                } catch (e: Exception) {
                    NetworkResult.Error(refreshTrigger, e, cachedData)
                }
            }
        }
    }*/

    init {
        refreshTrigger.tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = refreshTrigger.transformLatest {
        if (isLoaded) return@transformLatest
        emit(NetworkResult.Loading(cachedData, ::refresh))

        try {
            cachedData = api()
            isLoaded = true
            emit(NetworkResult.Success(cachedData!!, ::refresh))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(e, cachedData, ::refresh))
        }
    }

    fun refresh() {
        isLoaded = false
        refreshTrigger.tryEmit(Unit)
    }
}
