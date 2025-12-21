package com.gondev.domain.model

interface IMedialDetail {
    val genres: List<Genre>
    val homepage: String?
    val productionCompanies: List<ProductionCompany>
    val productionCountries: List<ProductionCountry>
    val spokenLanguages: List<SpokenLanguage>
    val status: String
    val tagline: String?
    val videos: List<Video>
}

data class BelongsToCollection(
    val id: Int = 0,
    val name: String = "",
    val posterPath: String? = null,
    val backdropPath: String? = null
)

data class Genre(
    val id: Int = 0,
    val name: String = ""
)

data class ProductionCompany(
    val id: Int = 0,
    val logoPath: String? = null,
    val name: String = "",
    val originCountry: String = ""
)

data class ProductionCountry(
    val iso31661: String = "", // ISO 코드이므로 String으로 처리
    val name: String = ""
)

data class SpokenLanguage(
    val englishName: String = "",
    val iso6391: String = "", // ISO 코드이므로 String으로 처리
    val name: String = ""
)

data class Video (
    val iso6391: String,
    val iso31661: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val publishedAt: String,
    val id: String,
)