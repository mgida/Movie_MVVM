package com.example.movie_mvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.movie_mvvm.data.MoviePagingDataSource

class MovieRepository {

    fun getMovies(query:String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingDataSource(query) }
        ).liveData
}