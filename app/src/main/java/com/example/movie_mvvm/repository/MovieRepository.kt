package com.example.movie_mvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.movie_mvvm.data.MoviePagingDataSource
import com.example.movie_mvvm.data.MovieSearchPagingDataSource
import com.example.movie_mvvm.data.database.MovieDao
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.data.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    val favouriteMoviesRepo: Flow<List<MovieModel>> = movieDao.getMoviesFromDB()

    fun getMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingDataSource(query) }
        ).liveData


    fun searchMovies(searchQuery: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieSearchPagingDataSource(searchQuery) }
        ).liveData

    suspend fun getMovieReviews(id: Int, apiKey: String) =
        RetrofitInstance.movieService.getMovieReviews(id = id, apiKey = apiKey)

    suspend fun getMovieCredits(id: Int, apiKey: String) =
        RetrofitInstance.movieService.getMovieCredits(id = id, apiKey = apiKey)

    suspend fun getMovieTrailers(id: Int, apiKey: String) =
        RetrofitInstance.movieService.getMovieTrailers(id = id, apiKey = apiKey)

    suspend fun insertMovie(movieModel: MovieModel) =
        movieDao.insertMovie(movieModel)

    suspend fun deleteMovie(movieModel: MovieModel) =
        movieDao.deleteMovie(movieModel)


    fun selectMovieById(id: Int) =
        movieDao.selectMovieByID(id)


}