package com.gondev.domain.model

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
): IMovieModel

interface IMovieModel: IMediaModel {
    val adult: Boolean
    val originalTitle: String
    val voteAverage: Double
    val voteCount: Int
}

