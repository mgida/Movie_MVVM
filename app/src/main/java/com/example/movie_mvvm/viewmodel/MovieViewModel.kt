package com.example.movie_mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.DataState
import kotlinx.coroutines.launch

const val API_KEY = ""

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies: MutableLiveData<DataState<List<MovieModel>>> =
        MutableLiveData<DataState<List<MovieModel>>>()

    val movies: LiveData<DataState<List<MovieModel>>> get() = _movies

    init {
        discoverMovies(apiKey = API_KEY)
    }

    private fun discoverMovies(apiKey: String) {
        viewModelScope.launch {
            _movies.postValue(DataState.Loading)
            try {
                val response = repository.discoverMovies(apiKey)
                val movieList = response.results
                _movies.postValue(DataState.Success(movieList))
            } catch (e: Exception) {
                _movies.postValue(DataState.Error(e))
            }
        }
    }
}