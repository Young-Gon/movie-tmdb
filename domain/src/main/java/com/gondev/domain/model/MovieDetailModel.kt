package com.gondev.domain.model

data class MovieDetailModel(
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
    override val adult: Boolean,
    override val originalTitle: String,
    override val voteAverage: Double,
    override val voteCount: Int,
    val belongsToCollection: BelongsToCollection?,
    val budget: Long,
    val imdbId: String?,
    val revenue: Long,
    val runtime: Int?,
): IMovieModel, IMedialDetail {
    companion object {
        fun createTestInstance(id: Int): MovieDetailModel {
            return MovieDetailModel(
                id = id,
                title = "Avengers: Endgame",
                originalTitle = "Avengers: Endgame",
                overview = "After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                posterPath = "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
                backdropPath = "/7RyMsIQuEAgbwSn8KspSOV1PS8u.jpg",
                releaseDate = "2019-04-24",
                genres = listOf(
                    Genre(12, "Adventure"),
                    Genre(878, "Science Fiction"),
                    Genre(28, "Action")
                ),
                adult = false,
                originalLanguage = "en",
                popularity = 100.0,
                voteAverage = 8.3,
                voteCount = 20000,
                homepage = "https://www.marvel.com/movies/avengers-endgame",
                productionCompanies = listOf(
                    ProductionCompany(420, "/hUzeosd33nzE5MCNsZxCGEKTXaQ.png", "Marvel Studios", "US")
                ),
                productionCountries = listOf(
                    ProductionCountry("US", "United States of America")
                ),
                spokenLanguages = listOf(
                    SpokenLanguage("English", "en", "English")
                ),
                status = "Released",
                tagline = "Part of the journey is the end.",
                videos = listOf(
                    Video(
                        iso6391 = "en",
                        iso31661 = "US",
                        name = "Official Trailer",
                        key = "TcMBFSGVi1c",
                        site = "YouTube",
                        size = 1080,
                        type = "Trailer",
                        official = true,
                        publishedAt = "2019-03-14T12:00:01.000Z",
                        id = "5c8f85f10e0a262601c1c4f3"
                    )
                ),
                belongsToCollection = null,
                budget = 356000000,
                imdbId = "tt4154796",
                revenue = 2797800564,
                runtime = 181
            )
        }
    }
}
