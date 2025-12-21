package com.gondev.networkfetcher

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.transformLatest

class NetworkFetcher<R>(
    private val api: suspend FlowCollector<NetworkResult<R>>.() -> R,
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    private var cachedData: R? = null

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = refreshTrigger.transformLatest {
        emit(NetworkResult.Loading(refreshTrigger, cachedData))

        try {
            cachedData = api()
            emit(NetworkResult.Success(refreshTrigger, cachedData))
        } catch (e: Exception) {
            emit(NetworkResult.Error(refreshTrigger, e, cachedData))
        }
    }
}