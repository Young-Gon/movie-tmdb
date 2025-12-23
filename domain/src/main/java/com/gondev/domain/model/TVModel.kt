package com.gondev.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TVModel(
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
) : ITVModel

@Serializable
sealed interface ITVModel : IMediaModel {
    val originCountry: List<String>
    val originalName: String
}
