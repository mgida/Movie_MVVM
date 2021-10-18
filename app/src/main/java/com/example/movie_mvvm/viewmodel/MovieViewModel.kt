package com.example.movie_mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.Constant.Companion.POPULAR
import com.example.movie_mvvm.utils.Constant.Companion.TAG
import com.example.movie_mvvm.utils.Constant.Companion.TOP_RATED
import com.example.movie_mvvm.utils.Constant.Companion.UPCOMING

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    lateinit var responsePopular: LiveData<PagingData<MovieModel>>
    lateinit var responseTopRated: LiveData<PagingData<MovieModel>>
    lateinit var responseUpcoming: LiveData<PagingData<MovieModel>>

    init {
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()

    }

    private fun getUpcomingMovies() {
        try {
            responseUpcoming = repository.getMovies(query = UPCOMING).cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred $e")
        }
    }

    private fun getTopRatedMovies() {
        try {
            responseTopRated = repository.getMovies(query = TOP_RATED).cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred $e")
        }
    }

    private fun getPopularMovies() {
        try {
            responsePopular = repository.getMovies(query = POPULAR).cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred $e")
        }
    }
}