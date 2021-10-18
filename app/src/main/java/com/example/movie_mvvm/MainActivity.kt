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
import com.example.movie_mvvm.adapter.UpComingMovieAdapter
import com.example.movie_mvvm.databinding.ActivityMainBinding
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter
    private lateinit var upcomingMovieAdapter: UpComingMovieAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val movieRepository = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        initRecyclerViewPopular()
        initRecyclerViewTopRated()
        initRecyclerViewUpComing()

        binding.btnRetry.setOnClickListener {
            popularMovieAdapter.retry()
            topRatedMovieAdapter.retry()
            upcomingMovieAdapter.retry()
        }

        viewModel.responsePopular.observe(this, {
            popularMovieAdapter.submitData(lifecycle = lifecycle, it)
        })

        viewModel.responseTopRated.observe(this, {
            topRatedMovieAdapter.submitData(lifecycle = lifecycle, it)
        })

        viewModel.responseUpcoming.observe(this, {
            upcomingMovieAdapter.submitData(lifecycle = lifecycle, it)
        })

        managePopularMoviesStates()
        manageTopRatedMoviesStates()
        manageUpComingMoviesStates()

    }

    private fun manageUpComingMoviesStates() {
        upcomingMovieAdapter.addLoadStateListener { loadState ->
            binding.recyclerViewUpComing.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            manageViews(loadState)

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && upcomingMovieAdapter.itemCount < 1
            ) {
                binding.textViewNoResult.isVisible = true
                binding.recyclerViewUpComing.isVisible = false
            } else {
                binding.textViewNoResult.isVisible = false
            }

        }
    }

    private fun manageTopRatedMoviesStates() {
        topRatedMovieAdapter.addLoadStateListener { loadState ->
            binding.recyclerViewTopRated.isVisible =
                loadState.source.refresh is LoadState.NotLoading

            manageViews(loadState)

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && topRatedMovieAdapter.itemCount < 1
            ) {
                binding.textViewNoResult.isVisible = true
                binding.recyclerViewTopRated.isVisible = false
            } else {
                binding.textViewNoResult.isVisible = false
            }

        }
    }

    private fun managePopularMoviesStates() {
        popularMovieAdapter.addLoadStateListener { loadState ->
            binding.recyclerViewPopular.isVisible = loadState.source.refresh is LoadState.NotLoading

            manageViews(loadState)

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && popularMovieAdapter.itemCount < 1
            ) {
                binding.textViewNoResult.isVisible = true
                binding.recyclerViewPopular.isVisible = false
            } else {
                binding.textViewNoResult.isVisible = false
            }

        }
    }

    private fun manageViews(loadState: CombinedLoadStates) {
        binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        binding.textViewError.isVisible = loadState.source.refresh is LoadState.Error
        binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
    }

    private fun initRecyclerViewUpComing() {
        upcomingMovieAdapter = UpComingMovieAdapter()
        binding.recyclerViewUpComing.apply {
            adapter = upcomingMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { upcomingMovieAdapter.retry() },
                header = MovieLoadStateAdapter { upcomingMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerViewTopRated() {
        topRatedMovieAdapter = TopRatedMovieAdapter()
        binding.recyclerViewTopRated.apply {
            adapter = topRatedMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
                header = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerViewPopular() {
        popularMovieAdapter = PopularMovieAdapter()
        binding.recyclerViewPopular.apply {
            adapter = popularMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { popularMovieAdapter.retry() },
                header = MovieLoadStateAdapter { popularMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

}