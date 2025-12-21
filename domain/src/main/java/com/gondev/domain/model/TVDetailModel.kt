package com.gondev.domain.model

data class TVDetailModel(
    val adult: Boolean = false,
    override val backdropPath: String?,
    override val id: Int,
    override val originalLanguage: String,
    override val overview: String,
    override val popularity: Double,
    override val posterPath: String?,
    override val title: String,
    override val releaseDate: String,
    override val genres: List<Genre>,
    override val homepage: String?,
    override val productionCompanies: List<ProductionCompany>,
    override val productionCountries: List<ProductionCountry>,
    override val spokenLanguages: List<SpokenLanguage>,
    override val status: String,
    override val tagline: String?,
    override val videos: List<Video>,
    override val originCountry: List<String>,
    override val originalName: String,
    val createdBy: List<CreatedBy>,
    val episodeRunTime: List<Int>,
    val inProduction: Boolean,
    val languages: List<String>,
    val lastAirDate: String,
    val lastEpisodeToAir: LastEpisodeToAir?,
    val nextEpisodeToAir: LastEpisodeToAir?,
    val networks: List<Network>,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val seasons: List<Season>,
    val type: String,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
) : ITVModel, IMedialDetail

data class CreatedBy(
    val id: Int = 0,
    val creditId: String = "",
    val name: String = "",
    val gender: Int = 0,
    val profilePath: String? = null
)

data class LastEpisodeToAir(
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val airDate: String = "",
    val episodeNumber: Int = 0,
    val productionCode: String = "",
    val runtime: Int? = null,
    val seasonNumber: Int = 0,
    val showId: Int = 0,
    val stillPath: String? = null
)

data class Network(
    val id: Int = 0,
    val logoPath: String? = null,
    val name: String = "",
    val originCountry: String = ""
)

data class Season(
    val airDate: String? = null,
    val episodeCount: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    val posterPath: String? = null,
    val seasonNumber: Int = 0,
    val voteAverage: Double = 0.0
)