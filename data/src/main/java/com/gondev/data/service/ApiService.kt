package com.gondev.data.service

import com.gondev.data.model.MediaType
import com.gondev.data.model.Movie
import com.gondev.data.model.PageContainer
import com.gondev.data.model.TV
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language")
        language: String = "ko-KR",
        @Query("page")
        page: Int = 1,
        @Query("region")
        region: String = "KR"
    ): PageContainer<Movie>

    @GET("/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language")
        language: String = "ko-KR",
        @Query("page")
        page: Int = 1,
        @Query("region")
        region: String = "KR"
    ): PageContainer<Movie>

    @GET("/trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path("media_type")
        mediaType: MediaType,
        @Path("time_window")
        timeWindow: String = "week",
        @Query("language")
        language: String = "ko-KR",
    ): PageContainer<Movie>

    @GET("/search/{media_type}")
    suspend fun getSearch(
        @Path("media_type")
        mediaType: MediaType,
        @Query("query")
        query: String,
        @Query("language")
        language: String = "ko-KR",
        @Query("region")
        region: String = "KR"
    ): PageContainer<Movie>

    @GET("/{media_type}/{media_id}")
    suspend fun <R> getDetail(
        @Path("media_type")
        mediaType: MediaType,
        @Path("media_id")
        mediaId: Long,
        @Query("append_to_response")
        appendToResponse: String = "videos",
        @Query("language")
        language: String = "ko-KR",
    ): R

    @GET("/tv/airing_today")
    suspend fun getAiringToday(
        @Query("language")
        language: String = "ko-KR",
        @Query("page")
        page: Int = 1,
        @Query("timezone")
        timeZone: String = "Asia/Seoul"
    ): PageContainer<TV>

    @GET("/tv/popular")
    suspend fun getTopRatedTVs(
        @Query("language")
        language: String = "ko-KR",
        @Query("page")
        page: Int = 1,
    ): PageContainer<TV>
}

