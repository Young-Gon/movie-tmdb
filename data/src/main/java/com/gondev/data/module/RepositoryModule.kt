package com.gondev.data.module

import com.gondev.data.repository.MovieRepositoryImpl
import com.gondev.data.repository.TVRepositoryImpl
import com.gondev.domain.repository.MovieRepository
import com.gondev.domain.repository.TVRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // 앱 전체에서 사용하려면 SingletonComponent
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindMovieRepository(
        repository: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    fun bindTVRepository(
        repository: TVRepositoryImpl
    ): TVRepository

}