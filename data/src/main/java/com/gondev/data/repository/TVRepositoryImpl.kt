package com.gondev.data.repository

import com.gondev.data.model.MediaType
import com.gondev.data.service.ApiService
import com.gondev.domain.repository.TVRepository
import javax.inject.Inject

class TVRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TVRepository {
    override suspend fun getTrendingTVs() =
        apiService.getTVTrending().toDomain { it.toDomain() }

    override suspend fun getAiringToday() =
        apiService.getAiringToday().toDomain { it.toDomain() }

    override suspend fun getTopRatedTVs() =
        apiService.getTopRatedTVs().toDomain { it.toDomain() }

    override suspend fun getSearch(query: String) =
        apiService.getSearch(MediaType.tv, query).toDomain { it.toDomain() }

    override suspend fun getDetail(mediaId: Long) =
        apiService.getTvDetail( mediaId).toDomain()
}