package com.example.movie_mvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.movie_mvvm.data.MoviePagingDataSource
import com.example.movie_mvvm.data.MovieSearchPagingDataSource
import com.example.movie_mvvm.data.database.MovieDao
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.data.network.MovieService
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieService: MovieService, private val movieDao: MovieDao) {

    val favouriteMoviesRepo: Flow<List<MovieModel>> = movieDao.getMoviesFromDB()

    fun getMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingDataSource(movieService, query) }
        ).liveData

    fun searchMovies(searchQuery: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieSearchPagingDataSource(movieService, searchQuery) }
        ).liveData

    suspend fun getMovieReviews(id: Int, apiKey: String) =
        movieService.getMovieReviews(id = id, apiKey = apiKey)

    suspend fun getMovieCredits(id: Int, apiKey: String) =
        movieService.getMovieCredits(id = id, apiKey = apiKey)

    suspend fun getCastDetail(personId: Int, apiKey: String) =
        movieService.getCastDetail(personId = personId, apiKey = apiKey)

    suspend fun getMovieTrailers(id: Int, apiKey: String) =
        movieService.getMovieTrailers(id = id, apiKey = apiKey)

    suspend fun insertMovie(movieModel: MovieModel) =
        movieDao.insertMovie(movieModel)

    suspend fun deleteMovie(movieModel: MovieModel) =
        movieDao.deleteMovie(movieModel)

    fun selectMovieById(id: Int) =
        movieDao.selectMovieByID(id)

}