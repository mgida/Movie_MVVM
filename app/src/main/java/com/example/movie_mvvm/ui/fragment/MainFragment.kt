package com.example.movie_mvvm.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.MovieLoadStateAdapter
import com.example.movie_mvvm.adapter.PopularMovieAdapter
import com.example.movie_mvvm.adapter.TopRatedMovieAdapter
import com.example.movie_mvvm.adapter.UpComingMovieAdapter
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.FragmentMainBinding
import com.example.movie_mvvm.utils.Constant.Companion.AntiqueFont
import com.example.movie_mvvm.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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

    private lateinit var typeface: Typeface
    private val viewModel by viewModels<MovieViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMainBinding.bind(view)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.elevation = 0F

        typeface = Typeface.createFromAsset(requireActivity().assets, AntiqueFont)
        applyCustomFont()

        initPopularRecyclerView()
        initTopRatedRecyclerView()
        initUpcomingRecyclerView()

        binding.btnRetry.setOnClickListener {
            binding.apply {
                tvPopular.isVisible = true
                tvTopRated.isVisible = true
                tvUpComing.isVisible = true
            }
            popularMovieAdapter.retry()
            topRatedMovieAdapter.retry()
            upcomingMovieAdapter.retry()
        }

        viewModel.popularResponse.observe(viewLifecycleOwner, {
            hidePopularShimmer()
            popularMovieAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        })

        viewModel.topRatedResponse.observe(viewLifecycleOwner, {
            hideTopRatedShimmer()
            topRatedMovieAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        })

        viewModel.upcomingResponse.observe(viewLifecycleOwner, {
            hideUpComingShimmer()
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
            if (loadState.source.refresh is LoadState.Error) {
                binding.tvUpComing.isVisible = false
            }
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
            if (loadState.source.refresh is LoadState.Error) {
                binding.tvTopRated.isVisible = false
            }
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

            if (loadState.source.refresh is LoadState.Error) {
                binding.tvPopular.isVisible = false
            }
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
        //   binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        binding.shimmerFrameLayoutPopular.isVisible = loadState.source.refresh is LoadState.Loading
        binding.shimmerFrameLayoutTopRated.isVisible = loadState.source.refresh is LoadState.Loading
        binding.shimmerFrameLayoutUpComing.isVisible = loadState.source.refresh is LoadState.Loading
        binding.textViewError.isVisible = loadState.source.refresh is LoadState.Error
        binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
    }

    private fun hideUpComingShimmer() {
        binding.shimmerFrameLayoutUpComing.stopShimmer()
        binding.shimmerFrameLayoutUpComing.visibility = View.GONE
        binding.recyclerViewUpComing.visibility = View.VISIBLE
    }

    private fun hideTopRatedShimmer() {
        binding.shimmerFrameLayoutTopRated.stopShimmer()
        binding.shimmerFrameLayoutTopRated.visibility = View.GONE
        binding.recyclerViewTopRated.visibility = View.VISIBLE
    }

    private fun hidePopularShimmer() {
        binding.shimmerFrameLayoutPopular.stopShimmer()
        binding.shimmerFrameLayoutPopular.visibility = View.GONE
        binding.recyclerViewPopular.visibility = View.VISIBLE
    }

    private fun initUpcomingRecyclerView() {
        upcomingMovieAdapter = UpComingMovieAdapter(typeface, this)
        binding.recyclerViewUpComing.apply {
            adapter = upcomingMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { upcomingMovieAdapter.retry() },
                header = MovieLoadStateAdapter { upcomingMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initTopRatedRecyclerView() {
        topRatedMovieAdapter = TopRatedMovieAdapter(typeface, this)
        binding.recyclerViewTopRated.apply {
            adapter = topRatedMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
                header = MovieLoadStateAdapter { topRatedMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun initPopularRecyclerView() {
        popularMovieAdapter = PopularMovieAdapter(this)
        binding.recyclerViewPopular.apply {
            adapter = popularMovieAdapter.withLoadStateHeaderAndFooter(
                footer = MovieLoadStateAdapter { popularMovieAdapter.retry() },
                header = MovieLoadStateAdapter { popularMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    private fun applyCustomFont() {
        binding.tvPopular.typeface = typeface
        binding.tvTopRated.typeface = typeface
        binding.tvUpComing.typeface = typeface
    }

    override fun onItemClick(movieModel: MovieModel) {
        val action = MainFragmentDirections.actionMainFragmentToMovieDetailFragment(movieModel)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movies_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayoutPopular.startShimmer()
        binding.shimmerFrameLayoutTopRated.startShimmer()
        binding.shimmerFrameLayoutUpComing.startShimmer()
    }

    override fun onPause() {
        binding.shimmerFrameLayoutPopular.stopShimmer()
        binding.shimmerFrameLayoutTopRated.stopShimmer()
        binding.shimmerFrameLayoutUpComing.stopShimmer()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}