package com.example.movie_mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.data.model.ReviewModel
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.Constant.Companion.API_KEY
import com.example.movie_mvvm.utils.Constant.Companion.POPULAR
import com.example.movie_mvvm.utils.Constant.Companion.TAG
import com.example.movie_mvvm.utils.Constant.Companion.TOP_RATED
import com.example.movie_mvvm.utils.Constant.Companion.UPCOMING
import com.example.movie_mvvm.utils.DataState
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    lateinit var responsePopular: LiveData<PagingData<MovieModel>>
    lateinit var responseTopRated: LiveData<PagingData<MovieModel>>
    lateinit var responseUpcoming: LiveData<PagingData<MovieModel>>

    private var _movieReviews: MutableLiveData<DataState<List<ReviewModel>>> =
        MutableLiveData<DataState<List<ReviewModel>>>()
    val movieReviews: LiveData<DataState<List<ReviewModel>>> get() = _movieReviews

    init {
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()

    }

    private fun getUpcomingMovies() {
        responseUpcoming = getMoviesOf(type = UPCOMING)
    }

    private fun getTopRatedMovies() {
        responseTopRated = getMoviesOf(type = TOP_RATED)
    }

    private fun getPopularMovies() {
        responsePopular = getMoviesOf(type = POPULAR)
    }

    private fun getMoviesOf(type: String): LiveData<PagingData<MovieModel>> {
        var data: LiveData<PagingData<MovieModel>>? = null
        try {
            data = repository.getMovies(query = type).cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred $e")
        }
        return data!!
    }

    fun getMovieReviews(id: Int, apiKey: String = API_KEY) {
        viewModelScope.launch {
            _movieReviews.postValue(DataState.Loading)
            try {
                val response = repository.getMovieReviews(id = id, apiKey = apiKey)
                _movieReviews.postValue(DataState.Success(response.results))
            } catch (e: Exception) {
                Log.d(TAG, "error occurred $e")
                _movieReviews.postValue(DataState.Error(e))
            }
        }
    }

}