package com.gondev.data.repository

import com.gondev.data.model.MediaType
import com.gondev.data.model.TVDetail
import com.gondev.data.service.ApiService
import com.gondev.domain.repository.TVRepository
import com.gondev.networkfetcher.NetworkFetcher
import com.skylabs.carton.b2h.data.model.MutateFetcher
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

    override fun getSearch() = MutateFetcher {query ->
        apiService.getSearch(MediaType.tv, query).toDomain { it.toDomain() }
    }.flow

    override fun getDetail(mediaId: Long) = NetworkFetcher {
        apiService.getDetail<TVDetail>(MediaType.tv, mediaId).toDomain()
    }.flow
}