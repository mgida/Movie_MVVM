package com.example.movie_mvvm.repository

import com.example.movie_mvvm.data.model.MovieResponse
import com.example.movie_mvvm.data.network.RetrofitInstance

class MovieRepository {

    suspend fun discoverMovies(apiKey: String): MovieResponse =
        RetrofitInstance.movieService.discoverMovies(apiKey = apiKey)
}