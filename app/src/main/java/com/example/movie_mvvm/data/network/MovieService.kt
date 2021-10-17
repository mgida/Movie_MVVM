package com.example.movie_mvvm.data.network

import com.example.movie_mvvm.data.model.MovieResponse
import com.example.movie_mvvm.utils.Constant.Companion.POPULAR
import com.example.movie_mvvm.utils.Constant.Companion.TOP_RATED
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{query}")
    suspend fun getMovies(
        @Path("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
    ): MovieResponse
}