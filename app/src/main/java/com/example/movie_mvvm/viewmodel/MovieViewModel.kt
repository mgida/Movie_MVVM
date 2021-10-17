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

//    private val _movies: MutableLiveData<DataState<PagingData<MovieModel>>> =
//        MutableLiveData<DataState<PagingData<MovieModel>>>()
//    val movies: LiveData<DataState<PagingData<MovieModel>>> get() = _movies

    lateinit var response: LiveData<PagingData<MovieModel>>

    init {
        discoverMovies()
    }

    private fun discoverMovies() {
        //  _movies.postValue(DataState.Loading)
        try {
            response = repository.discoverMovies().cachedIn(viewModelScope)
            //   _movies.postValue(DataState.Success(response))
        } catch (e: Exception) {
            //   _movies.postValue(DataState.Error(e))
            Log.d(TAG, "error occurred $e")
        }
    }
}