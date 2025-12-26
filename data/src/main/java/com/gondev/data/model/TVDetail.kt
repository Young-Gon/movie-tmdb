package com.gondev.data.model

import com.gondev.domain.model.TVDetailModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.gondev.domain.model.CreatedBy as DomainCreatedBy
import com.gondev.domain.model.LastEpisodeToAir as DomainLastEpisodeToAir
import com.gondev.domain.model.Network as DomainNetwork
import com.gondev.domain.model.Season as DomainSeason


@Serializable
data class TVDetail(
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("created_by")
    val createdBy: List<CreatedBy> = emptyList(),
    @SerialName("episode_run_time")
    val episodeRunTime: List<Int> = emptyList(),
    @SerialName("first_air_date")
    val firstAirDate: String = "",
    val genres: List<Genre> = emptyList(),
    val homepage: String? = null,
    val id: Int = 0,
    @SerialName("in_production")
    val inProduction: Boolean = false,
    val languages: List<String> = emptyList(),
    @SerialName("last_air_date")
    val lastAirDate: String = "",
    @SerialName("last_episode_to_air")
    val lastEpisodeToAir: LastEpisodeToAir? = null,
    val name: String = "",
    @SerialName("next_episode_to_air")
    val nextEpisodeToAir: LastEpisodeToAir? = null, // Can be null, same structure as last_episode_to_air
    val networks: List<Network> = emptyList(),
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int = 0,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int = 0,
    @SerialName("origin_country")
    val originCountry: List<String> = emptyList(),
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_name")
    val originalName: String = "",
    val overview: String? = null,
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany> = emptyList(),
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry> = emptyList(),
    val seasons: List<Season> = emptyList(),
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage> = emptyList(),
    val status: String = "",
    val tagline: String? = null,
    val type: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    val videos: VideoResult = VideoResult(),
) {
    fun toDomain() = TVDetailModel(
        adult = adult,
        backdropPath = backdropPath,
        createdBy = createdBy.map { it.toDomain() },
        episodeRunTime = episodeRunTime,
        genres = genres.map { it.toDomain() },
        homepage = homepage,
        id = id,
        inProduction = inProduction,
        languages = languages,
        lastAirDate = lastAirDate,
        lastEpisodeToAir = lastEpisodeToAir?.toDomain(),
        nextEpisodeToAir = nextEpisodeToAir?.toDomain(),
        networks = networks.map { it.toDomain() },
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originCountry = originCountry,
        originalLanguage = originalLanguage,
        originalName = originalName,
        overview = overview ?: "",
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies.map { it.toDomain() },
        productionCountries = productionCountries.map { it.toDomain() },
        seasons = seasons.map { it.toDomain() },
        spokenLanguages = spokenLanguages.map { it.toDomain() },
        status = status,
        tagline = tagline ?: "",
        type = type,
        voteAverage = voteAverage,
        voteCount = voteCount,
        videos = videos.results.map { it.toDomain() },
        title = name,
        releaseDate = firstAirDate,
    )
}

@Serializable
data class CreatedBy(
    val id: Int = 0,
    @SerialName("credit_id")
    val creditId: String = "",
    val name: String = "",
    val gender: Int = 0,
    @SerialName("profile_path")
    val profilePath: String? = null
) {
    fun toDomain(): DomainCreatedBy = DomainCreatedBy(
        id = id,
        creditId = creditId,
        name = name,
        gender = gender,
        profilePath = profilePath
    )
}

@Serializable
data class LastEpisodeToAir(
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("air_date")
    val airDate: String = "",
    @SerialName("episode_number")
    val episodeNumber: Int = 0,
    @SerialName("production_code")
    val productionCode: String = "",
    val runtime: Int? = null,
    @SerialName("season_number")
    val seasonNumber: Int = 0,
    @SerialName("show_id")
    val showId: Int = 0,
    @SerialName("still_path")
    val stillPath: String? = null
) {
    fun toDomain(): DomainLastEpisodeToAir = DomainLastEpisodeToAir(
        id = id,
        name = name,
        overview = overview,
        voteAverage = voteAverage,
        voteCount = voteCount,
        airDate = airDate,
        episodeNumber = episodeNumber,
        productionCode = productionCode,
        runtime = runtime,
        seasonNumber = seasonNumber,
        showId = showId,
        stillPath = stillPath
    )
}

@Serializable
data class Network(
    val id: Int = 0,
    @SerialName("logo_path")
    val logoPath: String? = null,
    val name: String = "",
    @SerialName("origin_country")
    val originCountry: String = ""
) {
    fun toDomain(): DomainNetwork = DomainNetwork(
        id = id,
        logoPath = logoPath,
        name = name,
        originCountry = originCountry
    )
}

@Serializable
data class Season(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_count")
    val episodeCount: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("season_number")
    val seasonNumber: Int = 0,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0
) {
    fun toDomain(): DomainSeason = DomainSeason(
        airDate = airDate,
        episodeCount = episodeCount,
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        seasonNumber = seasonNumber,
        voteAverage = voteAverage
    )
}
