package com.gondev.data.repository

import com.gondev.data.model.MediaType
import com.gondev.data.service.ApiService
import com.gondev.domain.repository.TVRepository
import com.gondev.networkfetcher.MutateFetcher
import com.gondev.networkfetcher.NetworkFetcher
import javax.inject.Inject

class TVRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TVRepository {
    override fun getTrendingTVs() = NetworkFetcher {
        apiService.getTrending(MediaType.tv).toDomain { it.toDomain() }
    }.flow

    override fun getAiringToday() = NetworkFetcher {
        apiService.getAiringToday().toDomain { it.toDomain() }
    }.flow

    override fun getTopRatedTVs() = NetworkFetcher {
        apiService.getTopRatedTVs().toDomain { it.toDomain() }
    }.flow

    override fun getSearch() = MutateFetcher { query ->
        apiService.getSearch(MediaType.tv, query).toDomain { it.toDomain() }
    }.flow

    override fun getDetail(mediaId: Long) = NetworkFetcher {
        apiService.getTvDetail( mediaId).toDomain()
    }.flow
}