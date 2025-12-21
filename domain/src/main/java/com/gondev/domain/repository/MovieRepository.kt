package com.gondev.domain.repository

import com.gondev.domain.model.MovieDetailModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer
import com.gondev.networkfetcher.MutateResult
import com.gondev.networkfetcher.NetworkResult
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getNowPlayingMovies(): Flow<NetworkResult<out PageContainer<MovieModel>?>>
    fun getUpcomingMovies(): Flow<NetworkResult<out PageContainer<MovieModel>?>>
    fun getTrendingMovie(): Flow<NetworkResult<out PageContainer<MovieModel>?>>
    fun getSearch(): Flow<MutateResult<String, out PageContainer<MovieModel>?>>
    fun getDetail(mediaId: Long): Flow<NetworkResult<out MovieDetailModel?>>
}