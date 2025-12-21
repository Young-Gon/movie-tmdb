package com.gondev.domain.usecase

import com.gondev.domain.repository.MovieRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMovieFeedUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    /*operator fun invoke(): Flow<NetworkFetcher<List<PageContainer<MovieModel>?>>> {
        return combine(
            repository.getNowPlayingMovies(),
            repository.getUpcomingMovies(),
            repository.getTrendingMovie()
        ) {  nowPlaying, upcoming,trending ->

            // 전략: 하나라도 로딩 중이면 로딩 상태 반환
            if (trending is NetworkResult.Loading ||
                nowPlaying is NetworkResult.Loading ||
                upcoming is NetworkResult.Loading) {
                NetworkResult.Loading
            }
            // 전략: 모두 성공했을 때만 Success 반환
            else if (trending is NetworkResult.Success &&
                nowPlaying is NetworkResult.Success &&
                upcoming is NetworkResult.Success) {
                NetworkResult.Success(
                    HomeContent(trending.data, nowPlaying.data, upcoming.data)
                )
            }
            // 전략: 하나라도 에러가 나면 에러 상태 반환 (혹은 로직에 따라 부분 성공 처리)
            else {
                val error = (trending as? NetworkResult.Error)?.exception
                    ?: (nowPlaying as? NetworkResult.Error)?.exception
                    ?: (upcoming as? NetworkResult.Error)?.exception
                    ?: Exception("Unknown Error")
                NetworkResult.Error(error)
            }
        }
    }*/
    operator fun invoke() = combine(
            repository.getNowPlayingMovies(),
            repository.getUpcomingMovies(),
            repository.getTrendingMovie()
        ) { nowPlaying, upcoming, trending ->
        }
    /*operator fun invoke() = NetworkFetcher {
        combine(
            repository.getNowPlayingMovies(),
            repository.getUpcomingMovies(),
            repository.getTrendingMovie()
        ) { nowPlaying, upcoming, trending ->
            trending.hasException { throw it }
            nowPlaying.hasException { throw it }
            upcoming.hasException { throw it }

            if(trending is NetworkResult.Success &&
                nowPlaying is NetworkResult.Success &&
                upcoming is NetworkResult.Success){
                    listOf(trending.data, nowPlaying.data, upcoming.data)
            }
        }
    }.flow*/
}