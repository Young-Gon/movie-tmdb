package com.gondev.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TVModel(
    override val adult: Boolean,
    override val backdropPath: String?,
    val genreIds: List<Int>,
    override val id: Int,
    override val originalLanguage: String,
    override val overview: String,
    override val popularity: Double,
    override val posterPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    override val title: String,
    override val releaseDate: String,
    override val originCountry: List<String>,
    override val originalName: String
) : ITVModel {
    companion object {
        fun createTestInstance(id: Int = 1) = TVModel(
            id = 1,
            title = "Preview TV Show",
            overview = "This is a preview overview.",
            posterPath = null,
            backdropPath = null,
            voteAverage = 8.5,
            releaseDate = "2024-01-01",
            genreIds = listOf(1, 2),
            adult = false,
            originalLanguage = "en",
            popularity = 100.0,
            voteCount = 1000,
            originalName = "Preview TV Show",
            originCountry = listOf("US")
        )
    }
}

@Serializable
sealed interface ITVModel : IMediaModel {
    val originCountry: List<String>
    val originalName: String
}
