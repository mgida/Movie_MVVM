package com.example.movie_mvvm.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.MovieLoadStateAdapter
import com.example.movie_mvvm.adapter.SearchMovieAdapter
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.FragmentMovieSearchBinding
import com.example.movie_mvvm.utils.Constant
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchFragment : Fragment(R.layout.fragment_movie_search),
    SearchMovieAdapter.OnItemClickListener {

    private var _binding: FragmentMovieSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var searchMovieAdapter: SearchMovieAdapter
    private lateinit var typeface: Typeface


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieSearchBinding.bind(view)

        binding.searchView.apply {
            setIconifiedByDefault(false)
            requestFocus()
        }
        typeface = Typeface.createFromAsset(requireActivity().assets, Constant.AntiqueFont)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        sharedViewModel.bundleFromFragmentDetailToFragmentSearch.observe(
            viewLifecycleOwner,
            { query ->
                if (query != null) {
                    searchMovies(query)
                }
            })

        initSearchRecyclerView()

        binding.btnRetrySearch.setOnClickListener {
            searchMovieAdapter.retry()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.recyclerViewSearch.scrollToPosition(0)
                    searchMovies(query)
                    sharedViewModel.bundleFromFragmentDetailToFragmentSearch.postValue(query)

                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun searchMovies(query: String) {
        viewModel.searchMovies(query = query)

        viewModel.searchResponse.observe(viewLifecycleOwner, {
            searchMovieAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        })

        manageSearchMoviesStates()
    }

    private fun manageSearchMoviesStates() {
        binding.textViewNoResultSearch.typeface = typeface
        searchMovieAdapter.addLoadStateListener { loadState ->
            binding.recyclerViewSearch.isVisible = loadState.source.refresh is LoadState.NotLoading

            manageViews(loadState)

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && searchMovieAdapter.itemCount < 1
            ) {
                binding.textViewNoResultSearch.isVisible = true
                binding.recyclerViewSearch.isVisible = false
            } else {
                binding.textViewNoResultSearch.isVisible = false
            }

        }
    }

    private fun manageViews(loadState: CombinedLoadStates) {
        binding.progressBarSearch.isVisible = loadState.source.refresh is LoadState.Loading
        binding.textViewErrorSearch.isVisible = loadState.source.refresh is LoadState.Error
        binding.btnRetrySearch.isVisible = loadState.source.refresh is LoadState.Error
    }

    private fun initSearchRecyclerView() {
        searchMovieAdapter = SearchMovieAdapter(typeface, this)
        binding.recyclerViewSearch.apply {
            adapter = searchMovieAdapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter { searchMovieAdapter.retry() },
            )
            setHasFixedSize(true)
        }
    }

    override fun onItemClick(movieModel: MovieModel) {
        val action =
            MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailFragment(movieModel)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

