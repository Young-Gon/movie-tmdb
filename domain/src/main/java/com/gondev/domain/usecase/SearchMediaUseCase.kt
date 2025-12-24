package com.gondev.domain.usecase

import com.gondev.domain.repository.MovieRepository
import com.gondev.domain.repository.TVRepository
import com.gondev.networkfetcher.MutateFetcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SearchMediaUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository
) {

    operator fun invoke() = MutateFetcher { query: String ->
        coroutineScope {
            val searchMovieDeferred = async { movieRepository.getSearch(query) }
            val searchTVDeferred = async { tvRepository.getSearch(query) }

            val searchMovie = searchMovieDeferred.await()
            val searchTV = searchTVDeferred.await()

            searchMovie to searchTV
        }
    }.flow
}