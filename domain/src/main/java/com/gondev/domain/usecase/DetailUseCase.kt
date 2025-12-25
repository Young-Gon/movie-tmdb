package com.gondev.domain.usecase

import com.gondev.domain.model.IMediaModel
import com.gondev.domain.model.MovieModel
import com.gondev.domain.repository.MovieRepository
import com.gondev.domain.repository.TVRepository
import com.gondev.networkfetcher.NetworkFetcher
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository,
) {
    operator fun invoke(mediaModel: IMediaModel) = NetworkFetcher(mediaModel) {
        if(mediaModel is MovieModel)
            movieRepository.getDetail(mediaModel.id)
        else
            tvRepository.getDetail(mediaModel.id)
    }.flow
}