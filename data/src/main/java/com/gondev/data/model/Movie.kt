package com.gondev.data.model

import com.gondev.domain.model.MovieModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    val id: Int = 0,
    @SerialName("original_language")
    val originalLanguage: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("original_title")
    val originalTitle: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false
) {
    fun toDomain(): MovieModel = MovieModel(
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        adult = adult,
        originalTitle = originalTitle,
        releaseDate = releaseDate,
        title = title,
        video = video
    )
}