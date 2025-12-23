package com.gondev.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    override val backdropPath: String?,
    val genreIds: List<Int>,
    override val id: Int,
    override val originalLanguage: String,
    override val overview: String,
    override val popularity: Double,
    override val posterPath: String?,
    override val voteAverage: Double,
    override val voteCount: Int,
    override val title: String,
    override val releaseDate: String,
    override val adult: Boolean,
    override val originalTitle: String,
    val video: Boolean,
) : IMovieModel {
    companion object {
        fun createTestInstance(id: Int = 1) = MovieModel(
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop_path_test.jpg",
            genreIds = listOf(28, 12, 878),
            id = id,
            originalLanguage = "en",
            overview = "이것은 테스트 영화에 대한 개요입니다. 흥미롭고 재미있는 내용이 가득합니다.",
            popularity = 123.45,
            posterPath = "https://image.tmdb.org/t/p/w500/poster_path_test.jpg",
            voteAverage = 7.8,
            voteCount = 1234,
            title = "테스트 영화 제목 $id",
            releaseDate = "2023-10-26",
            adult = false,
            originalTitle = "Test Movie Title $id",
            video = false
        )
    }
}

@Serializable
sealed interface IMovieModel : IMediaModel {
    val adult: Boolean
    val originalTitle: String
    val voteAverage: Double
    val voteCount: Int
}
