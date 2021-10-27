package com.example.movie_mvvm.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.MovieLoadStateAdapter
import com.example.movie_mvvm.adapter.PopularMovieAdapter
import com.example.movie_mvvm.adapter.TopRatedMovieAdapter
import com.example.movie_mvvm.adapter.UpComingMovieAdapter
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.FragmentMainBinding
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory

class MainFragment :
    Fragment(R.layout.fragment_main),
    PopularMovieAdapter.OnItemClickListener,
    TopRatedMovieAdapter.OnItemClickListener,
    UpComingMovieAdapter.OnItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter
    private lateinit var upcomingMovieAdapter: UpComingMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

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

        viewModel.responsePopular.observe(viewLifecycleOwner, {
            popularMovieAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        })

        viewModel.responseTopRated.observe(viewLifecycleOwner, {
            topRatedMovieAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        })

        viewModel.responseUpcoming.observe(viewLifecycleOwner, {
            upcomingMovieAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        })

        managePopularMoviesStates()
        manageTopRatedMoviesStates()
        manageUpComingMoviesStates()

        setHasOptionsMenu(true)
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
        upcomingMovieAdapter = UpComingMovieAdapter(this)
        binding.recyclerViewUpComing.apply {
            adapter = upcomingMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { upcomingMovieAdapter.retry() },
                header = MovieLoadStateAdapter { upcomingMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerViewTopRated() {
        topRatedMovieAdapter = TopRatedMovieAdapter(this)
        binding.recyclerViewTopRated.apply {
            adapter = topRatedMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
                header = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerViewPopular() {
        popularMovieAdapter = PopularMovieAdapter(this)
        binding.recyclerViewPopular.apply {
            adapter = popularMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { popularMovieAdapter.retry() },
                header = MovieLoadStateAdapter { popularMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    override fun onItemClick(movieModel: MovieModel) {
        val action = MainFragmentDirections.actionMainFragmentToMovieDetailFragment(movieModel)
        findNavController().navigate(action)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_movies_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.search_movies_action -> {
                navigateToSearchFragment()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToSearchFragment() {
        val action = MainFragmentDirections.actionMainFragmentToMovieSearchFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}