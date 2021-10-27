package com.example.movie_mvvm.data.network

import com.example.movie_mvvm.data.model.MovieResponse
import com.example.movie_mvvm.data.model.cast.CastResponse
import com.example.movie_mvvm.data.model.review.ReviewResponse
import com.example.movie_mvvm.data.model.trailer.TrailerResponse
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


    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
    ): ReviewResponse

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
    ): CastResponse

    @GET("movie/{id}/videos")
    suspend fun getMovieTrailers(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
    ): TrailerResponse

    @GET("search/movie/")
    suspend fun searchMovies(
        @Query("query") querySearch: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
    ): MovieResponse

}
