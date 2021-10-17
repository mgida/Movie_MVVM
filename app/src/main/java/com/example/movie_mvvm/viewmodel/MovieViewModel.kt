package com.example.movie_mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.Constant.Companion.TAG

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    lateinit var response: LiveData<PagingData<MovieModel>>

    init {
        discoverMovies()
    }

    private fun discoverMovies() {
        try {
            response = repository.discoverMovies().cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred $e")
        }
    }
}