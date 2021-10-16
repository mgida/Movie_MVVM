package com.example.movie_mvvm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.movie_mvvm.adapter.MovieAdapter
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.DataState
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "APP_DEBUG"

class MainActivity : AppCompatActivity() {
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieRepository = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MovieViewModel::class.java)

        initRecyclerView()

        viewModel.movies.observe(this, { dataState ->
            when (dataState) {

                is DataState.Success -> {
                    progress_bar.visibility = View.GONE
                    tvError.visibility = View.GONE
                    movieAdapter.differ.submitList(dataState.data)
                }
                is DataState.Loading -> {
                    progress_bar.visibility = View.VISIBLE
                }
                is DataState.Error -> {
                    progress_bar.visibility = View.GONE
                    dataState.exception.message?.let {
                        Log.d(TAG, "Error occurred $it")
                        tvError.visibility = View.VISIBLE
                        tvError.text = it
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        movieAdapter = MovieAdapter()
        recyclerView.apply {
            adapter = movieAdapter
            setHasFixedSize(true)
        }
    }
}