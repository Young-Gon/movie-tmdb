package com.gondev.domain.repository

import com.gondev.domain.model.PageContainer
import com.gondev.domain.model.TVDetailModel
import com.gondev.domain.model.TVModel

interface TVRepository {
    suspend fun getTrendingTVs(): PageContainer<TVModel>
    suspend fun getAiringToday(): PageContainer<TVModel>
    suspend fun getTopRatedTVs(): PageContainer<TVModel>
    suspend fun getSearch(query: String): PageContainer<TVModel>
    suspend fun getDetail(mediaId: Int): TVDetailModel
}