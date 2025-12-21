package com.gondev.data

import com.gondev.data.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_MovieModel(){
        val movie = Movie()
        println(movie)
        assertEquals(movie.adult, false)
    }
}