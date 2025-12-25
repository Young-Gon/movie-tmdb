package com.gondev.data.model

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDeserializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Test
    fun testMovieDeserialization() {
        val jsonString = """
            {
              "adult": false,
              "backdrop_path": "/9wXPKruA6bWYk2co5ix6fH59Qr8.jpg",
              "genre_ids": [
                12,
                878,
                28
              ],
              "id": 299534,
              "original_language": "en",
              "original_title": "Avengers: Endgame",
              "overview": "어벤져스의 패배 이후 지구는 초토화됐고 남은 절반의 사람들은 정신적 고통을 호소하며 하루하루를 근근이 버텨나간다.� 와칸다에서 싸우다 생존한 히어로들과 우주의 타이탄 행성에서 싸우다 생존한 히어로들이 뿔뿔이 흩어졌는데, 아이언맨과 네뷸라는 우주를 떠돌고 있고 지구에 남아 있는 어벤져스 멤버들은 닉 퓨리가 마지막에 신호를 보내다 만 송신기만 들여다보며 혹시 모를 우주의 응답을 그의 수집품이 되어버린 영웅과 악당을 모두 구할 수 있을까?",
              "popularity": 1.5446,
              "poster_path": "/cCIKixVHGIynoZR2xMg9uygey5f.jpg",
              "release_date": "2023-11-27",
              "title": "레고 마블 어벤져스: 코드 레드",
              "video": false,
              "vote_average": 6.493,
              "vote_count": 145
            }
        """.trimIndent()

        val movie = json.decodeFromString<Movie>(jsonString)

        assertEquals(false, movie.adult)
        assertEquals("/9wXPKruA6bWYk2co5ix6fH59Qr8.jpg", movie.backdropPath)
        assertEquals(listOf(12, 878, 28), movie.genreIds)
        assertEquals(299534, movie.id)
        assertEquals("en", movie.originalLanguage)
        assertEquals("Avengers: Endgame", movie.originalTitle)
        assertEquals("어벤져스의 패배 이후 지구는 초토화됐고 남은 절반의 사람들은 정신적 고통을 호소하며 하루하루를 근근이 버텨나간다.� 와칸다에서 싸우다 생존한 히어로들과 우주의 타이탄 행성에서 싸우다 생존한 히어로들이 뿔뿔이 흩어졌는데, 아이언맨과 네뷸라는 우주를 떠돌고 있고 지구에 남아 있는 어벤져스 멤버들은 닉 퓨리가 마지막에 신호를 보내다 만 송신기만 들여다보며 혹시 모를 우주의 응답을 그의 수집품이 되어버린 영웅과 악당을 모두 구할 수 있을까?", movie.overview)
        assertEquals(1.5446, movie.popularity, 0.0)
        assertEquals("/cCIKixVHGIynoZR2xMg9uygey5f.jpg", movie.posterPath)
        assertEquals("2023-11-27", movie.releaseDate)
        assertEquals("레고 마블 어벤져스: 코드 레드", movie.title)
        assertEquals(false, movie.video)
        assertEquals(6.493, movie.voteAverage, 0.0)
        assertEquals(145, movie.voteCount)
    }
}
