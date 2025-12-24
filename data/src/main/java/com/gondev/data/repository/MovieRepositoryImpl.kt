package com.gondev.data.repository

import com.gondev.data.service.ApiService
import com.gondev.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {
    override suspend fun getNowPlayingMovies() =
        apiService.getNowPlayingMovies().toDomain { it.toDomain() }

    override suspend fun getUpcomingMovies() =
        apiService.getUpcomingMovies().toDomain { it.toDomain() }

    override suspend fun getTrendingMovie() =
        apiService.getMovieTrending().toDomain { it.toDomain() }

    override suspend fun getSearch(query: String) =
        apiService.getSearchMovie(query).toDomain { it.toDomain() }

    override suspend fun getDetail(mediaId: Long) =
        apiService.getMovieDetail(mediaId).toDomain()
}
