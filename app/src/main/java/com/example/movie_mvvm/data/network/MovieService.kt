package com.example.movie_mvvm.data.network

import com.example.movie_mvvm.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

}