package com.gondev.domain.model

data class TVDetailModel(
    override val adult: Boolean = false,
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
) : ITVModel, IMedialDetail {
    companion object {
        fun createTestInstance(id: Int): TVDetailModel {
            return TVDetailModel(
                id = id,
                title = "Game of Thrones",
                originalName = "Game of Thrones",
                overview = "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
                posterPath = "/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg",
                backdropPath = "/rIe3PnM6S7IBUmvNwDkBMX0i9Ez.jpg",
                releaseDate = "2011-04-17", // first_air_date
                genres = listOf(
                    Genre(10765, "Sci-Fi & Fantasy"),
                    Genre(18, "Drama"),
                    Genre(10759, "Action & Adventure")
                ),
                adult = false,
                originalLanguage = "en",
                popularity = 500.0,
                homepage = "http://www.hbo.com/game-of-thrones",
                productionCompanies = listOf(
                    ProductionCompany(76043, "/98bC4RBp3jccg26GSdCS2aGf02Z.png", "HBO", "US")
                ),
                productionCountries = listOf(
                    ProductionCountry("US", "United States of America")
                ),
                spokenLanguages = listOf(
                    SpokenLanguage("English", "en", "English")
                ),
                status = "Ended",
                tagline = "Winter is coming.",
                videos = emptyList(), // Can add a trailer later if needed
                originCountry = listOf("US"),
                createdBy = listOf(
                    CreatedBy(id = 9813, name = "David Benioff"),
                    CreatedBy(id = 228068, name = "D. B. Weiss")
                ),
                episodeRunTime = listOf(60),
                inProduction = false,
                languages = listOf("en"),
                lastAirDate = "2019-05-19",
                lastEpisodeToAir = LastEpisodeToAir(
                    id = 1551830,
                    name = "The Iron Throne",
                    overview = "In the aftermath of the devastating attack on King's Landing, Daenerys must face the survivors.",
                    voteAverage = 4.8,
                    voteCount = 217,
                    airDate = "2019-05-19",
                    episodeNumber = 6,
                    seasonNumber = 8,
                    showId = 1399
                ),
                nextEpisodeToAir = null,
                networks = listOf(
                    Network(49, "/tuomPhY2UtuPTqqFnKMVheVa7_s.png", "HBO", "US")
                ),
                numberOfEpisodes = 73,
                numberOfSeasons = 8,
                seasons = listOf(
                    Season(
                        airDate = "2011-04-17",
                        episodeCount = 10,
                        id = 3624,
                        name = "Season 1",
                        overview = "Trouble is brewing in the Seven Kingdoms of Westeros...",
                        posterPath = "/zwaj4egrhnXJ24NjC2F72a9sVfO.jpg",
                        seasonNumber = 1
                    )
                ),
                type = "Scripted",
                voteAverage = 8.4,
                voteCount = 15000
            )
        }
    }
}

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
