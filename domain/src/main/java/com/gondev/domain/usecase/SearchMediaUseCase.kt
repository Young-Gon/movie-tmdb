package com.gondev.domain.usecase

import com.gondev.domain.model.MediaType
import com.gondev.domain.repository.MovieRepository
import com.gondev.domain.repository.TVRepository
import javax.inject.Inject

class SearchMediaUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository
) {

    suspend operator fun invoke(query: String, mediaType: MediaType) =
        if (mediaType == MediaType.MOVIE)
            movieRepository.getSearch(query)
        else
            tvRepository.getSearch(query)
}