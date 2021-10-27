package com.example.movie_mvvm.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.data.model.MovieResponse
import com.example.movie_mvvm.data.network.RetrofitInstance
import com.example.movie_mvvm.utils.Constant.Companion.API_KEY
import com.example.movie_mvvm.utils.Constant.Companion.STARTING_POSITION
import com.example.movie_mvvm.utils.Constant.Companion.TAG

class MovieSearchPagingDataSource(private val query: String) : PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        val position = params.key ?: STARTING_POSITION
        return try {
            val response: MovieResponse =
                RetrofitInstance.movieService.searchMovies(
                    querySearch = query,
                    apiKey = API_KEY,
                    page = position
                )

            val movies: List<MovieModel> = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_POSITION) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.d(TAG, "Error occurred ${e.printStackTrace()}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition
    }

}