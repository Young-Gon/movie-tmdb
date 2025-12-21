package com.gondev.domain.model

interface IMediaModel {
    val backdropPath: String?
    val id: Int
    val originalLanguage: String
    val overview: String
    val popularity: Double
    val posterPath: String?
    val title: String
    val releaseDate: String
}