package com.gondev.domain.repository

import com.gondev.domain.model.MovieDetailModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.model.PageContainer

interface MovieRepository {
    suspend fun getNowPlayingMovies(): PageContainer<MovieModel>
    suspend fun getUpcomingMovies(): PageContainer<MovieModel>
    suspend fun getTrendingMovie(): PageContainer<MovieModel>
    suspend fun getSearch(query: String): PageContainer<MovieModel>
    suspend fun getDetail(mediaId: Long): MovieDetailModel
}