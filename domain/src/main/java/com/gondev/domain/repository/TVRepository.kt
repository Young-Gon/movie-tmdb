package com.gondev.domain.repository

import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer
import com.gondev.domain.model.TVDetailModel
import com.gondev.domain.model.TVModel
import com.gondev.networkfetcher.MutateResult
import com.gondev.networkfetcher.NetworkResult
import kotlinx.coroutines.flow.Flow

interface TVRepository {
    fun getTrendingTVs(): Flow<NetworkResult<out PageContainer<MovieModel>?>>
    fun getAiringToday(): Flow<NetworkResult<out PageContainer<TVModel>?>>
    fun getTopRatedTVs(): Flow<NetworkResult<out PageContainer<TVModel>?>>
    fun getSearch(): Flow<MutateResult<String, out PageContainer<MovieModel>?>>
    fun getDetail(mediaId: Long): Flow<NetworkResult<out TVDetailModel?>>
}