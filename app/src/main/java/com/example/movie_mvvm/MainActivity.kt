package com.example.movie_mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import com.example.movie_mvvm.adapter.MovieAdapter
import com.example.movie_mvvm.adapter.MovieLoadStateAdapter
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieRepository = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        initRecyclerView()

        btnRetry.setOnClickListener {
            movieAdapter.retry()
        }

        viewModel.response.observe(this, {
            movieAdapter.submitData(lifecycle = lifecycle, it)
        })

        manageStates()
    }

    private fun manageStates() {
        movieAdapter.addLoadStateListener { loadState ->
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            textViewError.isVisible = loadState.source.refresh is LoadState.Error
            btnRetry.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && movieAdapter.itemCount < 1
            ) {
                textViewNoResult.isVisible = true
                recyclerView.isVisible = false
            } else {
                textViewNoResult.isVisible = false
            }

        }
    }

    private fun initRecyclerView() {
        movieAdapter = MovieAdapter()
        recyclerView.apply {
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { movieAdapter.retry() },
                header = MovieLoadStateAdapter { movieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }
}