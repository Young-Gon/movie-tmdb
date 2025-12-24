package com.gondev.domain.usecase

import com.gondev.domain.repository.TVRepository
import com.gondev.networkfetcher.NetworkFetcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetTvFeedUseCase @Inject constructor(
    private val repository: TVRepository
) {
    operator fun invoke() = NetworkFetcher {
        coroutineScope {
            // 각 suspend 함수를 async로 비동기적으로 실행합니다.
            val airingTodayDeferred = async { repository.getAiringToday() }
            val topRateTvDeferred = async { repository.getTopRatedTVs() }
            val trendingDeferred = async { repository.getTrendingTVs() }

            // 각 async 작업의 결과를 기다립니다. 이들은 병렬로 실행됩니다.
            val airingToday = airingTodayDeferred.await()
            val topRateTv = topRateTvDeferred.await()
            val trending = trendingDeferred.await()

            // 세 가지 결과를 Triple로 묶어 반환합니다.
            // NetworkResult<T> 타입이라면, 여기서는 NetworkResult<Triple<...>>를 반환하도록 합니다.
            // 실제 데이터 통합 로직은 이 부분에 작성하시면 됩니다.
            Triple(airingToday, topRateTv, trending)
        }
    }.flow
}
