package com.gondev.networkfetcher.transform

import com.gondev.networkfetcher.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class NetworkFetcherTransformPair<R1, R2>(
    fetcher1: Flow<NetworkResult<out R1>>,
    fetcher2: Flow<NetworkResult<out R2>>
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    val flow: Flow<NetworkResult<Pair<R1?, R2?>>> = channelFlow {
        var result1: NetworkResult<out R1>? = null
        var result2: NetworkResult<out R2>? = null

        fun sendCombined() {
            val f1 = result1
            val f2 = result2

            // 로딩 상태 처리: 두 플로우 중 하나라도 값을 보내지 않았거나, 로딩 중일 때
            if (f1 == null || f2 == null || f1 is NetworkResult.Loading || f2 is NetworkResult.Loading) {
                trySend(NetworkResult.Loading(Pair(f1?.data, f2?.data), ::refresh))
                return
            }

            // 에러 상태 처리: 둘 중 하나라도 에러가 있으면 에러를 전파
            var exception: Exception? = null
            f1.hasException { exception = it }
            f2.hasException { if (exception == null) exception = it }

            if (exception != null) {
                trySend(NetworkResult.Error(exception, Pair(f1.data, f2.data), ::refresh))
                return
            }

            // 성공 상태 처리
            // f1.data와 f2.data가 null일 수 있으므로 Pair<R1?, R2?>를 생성합니다.
            // NetworkResult.Success의 제네릭 타입 T는 Pair<R1?, R2?>입니다.
            val combinedData = Pair(f1.data!!, f2.data!!)
            trySend(NetworkResult.Success(combinedData, ::refresh))
        }

        // refreshTrigger가 호출되면 원본 플로우들의 refresh를 호출
        launch {
            refreshTrigger.collect {
                result1?.refresh()
                result2?.refresh()
            }
        }

        // 각 플로우의 결과를 수신하고 결합된 결과를 전송
        launch {
            fetcher1.collect {
                result1 = it
                sendCombined()
            }
        }

        launch {
            fetcher2.collect {
                result2 = it
                sendCombined()
            }
        }
    }

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}

operator fun <R1, R2> Flow<NetworkResult<out R1>>.plus(other: Flow<NetworkResult<out R2>>) =
    NetworkFetcherTransformPair(this, other).flow
