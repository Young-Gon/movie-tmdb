package com.gondev.data.service

import com.gondev.data.model.MediaType
import com.gondev.data.model.Movie
import com.gondev.data.model.MovieDetail
import com.gondev.data.model.PageContainer
import com.gondev.data.model.TV
import com.gondev.data.model.TVDetail
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

    @GET("/trending/movie/{time_window}")
    suspend fun getMovieTrending(
        @Path("time_window")
        timeWindow: String = "week",
        @Query("language")
        language: String = "ko-KR",
    ): PageContainer<Movie>

    @GET("/trending/tv/{time_window}")
    suspend fun getTVTrending(
        @Path("time_window")
        timeWindow: String = "week",
        @Query("language")
        language: String = "ko-KR",
    ): PageContainer<TV>

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

    @GET("/movie/{media_id}")
    suspend fun getMovieDetail(
        @Path("media_id")
        mediaId: Long,
        @Query("append_to_response")
        appendToResponse: String = "videos",
        @Query("language")
        language: String = "ko-KR",
    ): MovieDetail

    @GET("/tv/{media_id}")
    suspend fun getTvDetail(
        @Path("media_id")
        mediaId: Long,
        @Query("append_to_response")
        appendToResponse: String = "videos",
        @Query("language")
        language: String = "ko-KR",
    ): TVDetail

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

