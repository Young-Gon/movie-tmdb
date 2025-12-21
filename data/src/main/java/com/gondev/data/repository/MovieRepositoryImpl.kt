package com.gondev.data.repository

import com.gondev.data.model.MediaType
import com.gondev.data.model.MovieDetail
import com.gondev.data.service.ApiService
import com.gondev.domain.repository.MovieRepository
import com.gondev.networkfetcher.NetworkFetcher
import com.skylabs.carton.b2h.data.model.MutateFetcher
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {
    override fun getNowPlayingMovies() = NetworkFetcher {
        apiService.getNowPlayingMovies().toDomain { it.toDomain() }
    }.flow

    override fun getUpcomingMovies() = NetworkFetcher {
        apiService.getUpcomingMovies().toDomain { it.toDomain() }
    }.flow

    override fun getTrendingMovie() = NetworkFetcher {
        apiService.getTrending(MediaType.movie).toDomain { it.toDomain() }
    }.flow

    override fun getSearch() = MutateFetcher { query ->
        apiService.getSearch(MediaType.movie, query).toDomain { it.toDomain() }
    }.flow

    override fun getDetail(mediaId: Long) = NetworkFetcher {
        apiService.getDetail<MovieDetail>(MediaType.movie, mediaId).toDomain()
    }.flow
}
