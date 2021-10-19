package com.example.movie_mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.MovieCastAdapter
import com.example.movie_mvvm.adapter.MovieReviewAdapter
import com.example.movie_mvvm.databinding.FragmentMovieDetailBinding
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.utils.Constant
import com.example.movie_mvvm.utils.Constant.Companion.TAG
import com.example.movie_mvvm.utils.DataState
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<MovieDetailFragmentArgs>()

    private lateinit var reviewAdapter: MovieReviewAdapter
    private lateinit var castAdapter: MovieCastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        val movieRepository = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        initRecyclerViewReview()
        initRecyclerViewCast()

        val movie = args.movie

        viewModel.getMovieReviews(id = movie.id)
        viewModel.getMovieCredits(id = movie.id)

        binding.apply {
            Glide.with(binding.root)
                .load("${Constant.IMAGE_URL}${movie.poster_path}")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageViewDetail)

            textViewTitleDetail.text = movie.title
            textViewDateDetail.text = movie.release_date
            textViewOverviewDetail.text = movie.overview
        }

        viewModel.movieReviews.observe(viewLifecycleOwner, { dataState ->

            when (dataState) {
                is DataState.Loading -> {
                    binding.progressBarReview.visibility = View.VISIBLE
                }
                is DataState.Success -> {
                    binding.progressBarReview.visibility = View.GONE
                    val reviews = dataState.data
                    reviewAdapter.differ.submitList(reviews)
                }
                is DataState.Error -> {
                    binding.progressBarReview.visibility = View.GONE
                    binding.recyclerViewReviews.visibility = View.GONE
                    Log.d(TAG, "error occurred ${dataState.exception.message}")
                }
            }
        })

        viewModel.movieCast.observe(viewLifecycleOwner, { dataState ->

            when (dataState) {
                is DataState.Loading -> {
                    binding.progressBarCast.visibility = View.VISIBLE
                }
                is DataState.Success -> {
                    binding.progressBarCast.visibility = View.GONE
                    val cast = dataState.data
                    castAdapter.differ.submitList(cast)
                }
                is DataState.Error -> {
                    binding.progressBarCast.visibility = View.GONE
                    binding.recyclerViewCast.visibility = View.GONE
                    Log.d(TAG, "error occurred ${dataState.exception.message}")
                }
            }
        })
    }

    private fun initRecyclerViewReview() {
        reviewAdapter = MovieReviewAdapter()
        binding.recyclerViewReviews.apply {
            adapter = reviewAdapter
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerViewCast() {
        castAdapter = MovieCastAdapter()
        binding.recyclerViewCast.apply {
            adapter = castAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}