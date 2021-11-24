package com.example.movie_mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.data.model.cast.CastModel
import com.example.movie_mvvm.data.model.review.ReviewModel
import com.example.movie_mvvm.data.model.trailer.TrailerModel
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.Constant.Companion.API_KEY
import com.example.movie_mvvm.utils.Constant.Companion.POPULAR
import com.example.movie_mvvm.utils.Constant.Companion.TAG
import com.example.movie_mvvm.utils.Constant.Companion.TOP_RATED
import com.example.movie_mvvm.utils.Constant.Companion.UPCOMING
import com.example.movie_mvvm.utils.DataState
import kotlinx.coroutines.launch


class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    lateinit var popularResponse: LiveData<PagingData<MovieModel>>
    lateinit var topRatedResponse: LiveData<PagingData<MovieModel>>
    lateinit var upcomingResponse: LiveData<PagingData<MovieModel>>

    val favourites: LiveData<List<MovieModel>> = repository.favouriteMoviesRepo.asLiveData()

    lateinit var searchResponse: LiveData<PagingData<MovieModel>>

    private var _movieReviews: MutableLiveData<DataState<List<ReviewModel>>> =
        MutableLiveData<DataState<List<ReviewModel>>>()
    val movieReviews: LiveData<DataState<List<ReviewModel>>> get() = _movieReviews

    private var _movieCast: MutableLiveData<DataState<List<CastModel>>> =
        MutableLiveData<DataState<List<CastModel>>>()
    val movieCast: LiveData<DataState<List<CastModel>>> get() = _movieCast

    private var _movieTrailers: MutableLiveData<DataState<List<TrailerModel>>> =
        MutableLiveData<DataState<List<TrailerModel>>>()
    val movieTrailers: LiveData<DataState<List<TrailerModel>>> get() = _movieTrailers

    init {
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }

    private fun getUpcomingMovies() {
        upcomingResponse = getMoviesOf(type = UPCOMING)
    }

    private fun getTopRatedMovies() {
        topRatedResponse = getMoviesOf(type = TOP_RATED)
    }

    private fun getPopularMovies() {
        popularResponse = getMoviesOf(type = POPULAR)
    }

    private fun getMoviesOf(type: String): LiveData<PagingData<MovieModel>> {
        var data: LiveData<PagingData<MovieModel>>? = null
        try {
            data = repository.getMovies(query = type).cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred ${e.printStackTrace()}")
        }
        return data!!
    }

    fun searchMovies(query: String) {
        try {
            searchResponse = repository.searchMovies(searchQuery = query).cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d(TAG, "error occurred ${e.printStackTrace()}")
        }
    }

    fun getMovieReviews(id: Int, apiKey: String = API_KEY) {
        viewModelScope.launch {
            _movieReviews.postValue(DataState.Loading)
            try {
                val response = repository.getMovieReviews(id = id, apiKey = apiKey)
                _movieReviews.postValue(DataState.Success(response.results))

            } catch (e: Exception) {
                Log.d(TAG, "error occurred ${e.printStackTrace()}")
                _movieReviews.postValue(DataState.Error(e))
            }
        }
    }

    fun getMovieCredits(id: Int, apiKey: String = API_KEY) {
        viewModelScope.launch {
            _movieCast.postValue(DataState.Loading)
            try {
                val response = repository.getMovieCredits(id = id, apiKey = apiKey)
                _movieCast.postValue(DataState.Success(response.cast))

            } catch (e: Exception) {
                Log.d(TAG, "error occurred ${e.printStackTrace()}")
                _movieCast.postValue(DataState.Error(e))
            }
        }
    }

    fun getMovieTrailers(id: Int, apiKey: String = API_KEY) {
        viewModelScope.launch {
            _movieTrailers.postValue(DataState.Loading)
            try {
                val response = repository.getMovieTrailers(id = id, apiKey = apiKey)
                _movieTrailers.postValue(DataState.Success(response.trailers))
            } catch (e: Exception) {
                Log.d(TAG, "error occurred ${e.printStackTrace()}")
                _movieTrailers.postValue(DataState.Error(e))
            }
        }
    }

    fun insertMovie(movieModel: MovieModel) {
        viewModelScope.launch {
            try {
                repository.insertMovie(movieModel)
            } catch (e: Exception) {
                Log.d(TAG, "error occurred ${e.printStackTrace()}")
            }
        }
    }

    fun deleteMovie(movieModel: MovieModel) {
        viewModelScope.launch {
            try {
                repository.deleteMovie(movieModel)
            } catch (e: Exception) {
                Log.d(TAG, "error occurred ${e.printStackTrace()}")
            }
        }
    }

    fun selectMovieById(id: Int) = repository.selectMovieById(id)
}

