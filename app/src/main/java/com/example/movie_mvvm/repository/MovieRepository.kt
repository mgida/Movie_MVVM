package com.example.movie_mvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.movie_mvvm.data.MoviePagingDataSource
import com.example.movie_mvvm.data.network.RetrofitInstance

class MovieRepository {

    fun getMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingDataSource(query) }
        ).liveData

    suspend fun getMovieReviews(id: Int, apiKey: String) =
        RetrofitInstance.movieService.getMovieReviews(id = id, apiKey = apiKey)

    suspend fun getMovieCredits(id: Int, apiKey: String) =
        RetrofitInstance.movieService.getMovieCredits(id = id, apiKey = apiKey)

    suspend fun getMovieTrailers(id: Int, apiKey: String) =
        RetrofitInstance.movieService.getMovieTrailers(id = id, apiKey = apiKey)

}