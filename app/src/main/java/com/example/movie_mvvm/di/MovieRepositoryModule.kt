package com.example.movie_mvvm.di

import com.example.movie_mvvm.data.database.MovieDao
import com.example.movie_mvvm.data.network.MovieService
import com.example.movie_mvvm.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRepositoryModule {

    @Provides
    @Singleton
    fun provideRepoDependencies(
        movieService: MovieService,
        movieDao: MovieDao
    ): MovieRepository =
        MovieRepository(
            movieService = movieService,
            movieDao = movieDao
        )
}