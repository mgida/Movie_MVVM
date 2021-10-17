package com.example.movie_mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.movie_mvvm.adapter.MovieLoadStateAdapter
import com.example.movie_mvvm.adapter.PopularMovieAdapter
import com.example.movie_mvvm.adapter.TopRatedMovieAdapter
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieRepository = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        initRecyclerViewPopular()
        initRecyclerViewTopRated()

        btnRetry.setOnClickListener {
            popularMovieAdapter.retry()
            topRatedMovieAdapter.retry()
        }

        viewModel.responsePopular.observe(this, {
            popularMovieAdapter.submitData(lifecycle = lifecycle, it)
        })

        viewModel.responseTopRated.observe(this, {
            topRatedMovieAdapter.submitData(lifecycle = lifecycle, it)
        })

        managePopularMoviesStates()
        manageTopRatedMoviesStates()
    }

    private fun manageTopRatedMoviesStates() {
        topRatedMovieAdapter.addLoadStateListener { loadState ->
            manageViews(loadState)

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && topRatedMovieAdapter.itemCount < 1
            ) {
                textViewNoResult.isVisible = true
                recyclerViewTopRated.isVisible = false
            } else {
                textViewNoResult.isVisible = false
            }

        }
    }

    private fun managePopularMoviesStates() {
        popularMovieAdapter.addLoadStateListener { loadState ->
            manageViews(loadState)

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && popularMovieAdapter.itemCount < 1
            ) {
                textViewNoResult.isVisible = true
                recyclerViewPopular.isVisible = false
            } else {
                textViewNoResult.isVisible = false
            }

        }
    }

    private fun manageViews(loadState: CombinedLoadStates) {
        progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        recyclerViewTopRated.isVisible = loadState.source.refresh is LoadState.NotLoading
        textViewError.isVisible = loadState.source.refresh is LoadState.Error
        btnRetry.isVisible = loadState.source.refresh is LoadState.Error
    }

    private fun initRecyclerViewTopRated() {
        topRatedMovieAdapter = TopRatedMovieAdapter()
        recyclerViewTopRated.apply {
            adapter = topRatedMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
                header = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerViewPopular() {
        popularMovieAdapter = PopularMovieAdapter()
        recyclerViewPopular.apply {
            adapter = popularMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { popularMovieAdapter.retry() },
                header = MovieLoadStateAdapter { popularMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

}