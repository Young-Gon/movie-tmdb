package com.gondev.data.model

import com.gondev.domain.model.MovieDetailModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.gondev.domain.model.Genre as DomainGenre
import com.gondev.domain.model.ProductionCompany as DomainProductionCompany
import com.gondev.domain.model.ProductionCountry as DomainProductionCountry
import com.gondev.domain.model.SpokenLanguage as DomainSpokenLanguage
import com.gondev.domain.model.Video as DomainVideo


@Serializable
data class MovieDetail(
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null, // null이 될 수 있으므로 nullable
    @SerialName("belongs_to_collection")
    val belongsToCollection: String? = null, // null이 될 수 있고, 객체이므로 별도 data class
    val budget: Long = 0, // 예산은 큰 숫자이므로 Long으로
    val genres: List<Genre> = emptyList(), // 리스트이므로 List<Genre>
    val homepage: String? = null, // null이 될 수 있으므로 nullable
    val id: Int = 0,
    @SerialName("imdb_id")
    val imdbId: String? = null, // null이 될 수 있으므로 nullable
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    val overview: String? = null, // null이 될 수 있으므로 nullable
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String? = null, // null이 될 수 있으므로 nullable
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany> = emptyList(),
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry> = emptyList(),
    @SerialName("release_date")
    val releaseDate: String = "",
    val revenue: Long = 0, // 수익은 큰 숫자이므로 Long으로
    val runtime: Int? = null, // null이 될 수 있으므로 nullable
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage> = emptyList(),
    val status: String = "",
    val tagline: String? = null, // null이 될 수 있으므로 nullable
    val title: String = "",
    val videos: List<Video> = emptyList(),
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
) {
    fun toDomain(): MovieDetailModel = MovieDetailModel(
        adult = adult,
        backdropPath = backdropPath,
        belongsToCollection = belongsToCollection,
        budget = budget,
        genres = genres.map { it.toDomain() },
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview ?: "",
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies.map { it.toDomain() },
        productionCountries = productionCountries.map { it.toDomain() },
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime ?: 0,
        spokenLanguages = spokenLanguages.map { it.toDomain() },
        status = status,
        tagline = tagline ?: "",
        title = title,
        videos = videos.map { it.toDomain() },
        voteAverage= voteAverage,
        voteCount = voteCount
    )
}

@Serializable
data class Genre(
    val id: Int = 0,
    val name: String = ""
) {
    fun toDomain(): DomainGenre = DomainGenre(
        id = id,
        name = name
    )
}

@Serializable
data class ProductionCompany(
    val id: Int = 0,
    @SerialName("logo_path")
    val logoPath: String? = null,
    val name: String = "",
    @SerialName("origin_country")
    val originCountry: String = ""
) {
    fun toDomain(): DomainProductionCompany = DomainProductionCompany(
        id = id,
        logoPath = logoPath,
        name = name,
        originCountry = originCountry
    )
}

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String = "", // ISO 코드이므로 String으로 처리
    val name: String = ""
) {
    fun toDomain(): DomainProductionCountry = DomainProductionCountry(
        iso31661 = iso31661,
        name = name
    )
}

@Serializable
data class SpokenLanguage(
    @SerialName("english_name")
    val englishName: String = "",
    @SerialName("iso_639_1")
    val iso6391: String = "", // ISO 코드이므로 String으로 처리
    val name: String = ""
) {
    fun toDomain(): DomainSpokenLanguage = DomainSpokenLanguage(
        englishName = englishName,
        iso6391 = iso6391,
        name = name
    )
}

@Serializable
data class Video (
    @SerialName("iso_639_1")
    val iso6391: String="",
    @SerialName("iso_3166_1")
    val iso31661: String="",
    val name: String="",
    val key: String="",
    val site: String="",
    val size: Int=0,
    val type: String="",
    val official: Boolean=false,
    @SerialName("published_at")
    val publishedAt: String="",
    val id: String="",
) {
    fun toDomain(): DomainVideo = DomainVideo(
        iso6391 = iso6391,
        iso31661 = iso31661,
        name = name,
        key = key,
        site = site,
        size = size,
        type = type,
        official = official,
        publishedAt = publishedAt,
        id = id,
    )
}