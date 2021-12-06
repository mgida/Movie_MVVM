package com.example.movie_mvvm.ui.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.MovieCastAdapter
import com.example.movie_mvvm.adapter.MovieReviewAdapter
import com.example.movie_mvvm.adapter.MovieTrailerAdapter
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.data.model.cast.CastModel
import com.example.movie_mvvm.data.model.trailer.TrailerModel
import com.example.movie_mvvm.databinding.FragmentMovieDetailBinding
import com.example.movie_mvvm.ui.YoutubeActivity
import com.example.movie_mvvm.utils.Constant
import com.example.movie_mvvm.utils.Constant.Companion.TAG
import com.example.movie_mvvm.utils.Constant.Companion.TRAILER
import com.example.movie_mvvm.utils.DataState
import com.example.movie_mvvm.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail),
    MovieTrailerAdapter.OnItemClickListener, MovieCastAdapter.OnItemClickListener {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<MovieDetailFragmentArgs>()

    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var reviewAdapter: MovieReviewAdapter
    private lateinit var castAdapter: MovieCastAdapter
    private lateinit var trailerAdapter: MovieTrailerAdapter
    private lateinit var typeface: Typeface
    private var toast: Toast? = null

    private var isFav = false
    private var idObserved = MutableLiveData<Int>()
    private lateinit var detailedMovie: MovieModel
    private var checkedId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)
        typeface = Typeface.createFromAsset(requireActivity().assets, Constant.AntiqueFont)
        detailedMovie = args.movie
        idObserved.postValue(detailedMovie.id)

        idObserved.observe(viewLifecycleOwner, {
            checkedId = it
        })

        viewModel.selectMovieById(detailedMovie.id).observe(viewLifecycleOwner, {
            if (it != null) {
                isFav = true
                binding.ivFav.setImageResource(R.drawable.ic_baseline_favorite_fill)
            }
        })

        applyCustomFont()

        populateUi(detailedMovie)

        initReviewRecyclerView()
        initCastRecyclerView()
        initTrailerRecyclerView()

        viewModel.getMovieReviews(id = detailedMovie.id)
        observeReviews()

        viewModel.getMovieCredits(id = detailedMovie.id)
        observeCast()

        viewModel.getMovieTrailers(id = detailedMovie.id)
        observeTrailers()
    }

    private fun toggleFavMovies(selectedMovie: MovieModel) {
        isFav = !isFav
        if (isFav) {
            addMovieToFav(selectedMovie)
        } else {
            removeMovieFromFav(selectedMovie)
        }
    }

    private fun removeMovieFromFav(selectedMovie: MovieModel) {
        isFav = false
        viewModel.deleteMovie(selectedMovie)
        binding.ivFav.setImageResource(R.drawable.ic_baseline_favorite_30)
        showToastMessage(message = "Deleted Successfully")
    }

    private fun addMovieToFav(selectedMovie: MovieModel) {
        isFav = true
        viewModel.insertMovie(selectedMovie)
        binding.ivFav.setImageResource(R.drawable.ic_baseline_favorite_fill)
        showToastMessage(message = "Saved Successfully")
    }

    private fun showToastMessage(message: String) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun observeTrailers() {
        viewModel.movieTrailers.observe(viewLifecycleOwner, { dataState ->

            when (dataState) {
                is DataState.Loading -> {
                    showTrailerRecyclerView(isDisplayed = false)
                    showTrailerProgressbar(isDisplayed = true)
                }
                is DataState.Success -> {
                    if (detailedMovie.id == checkedId) {
                        showTrailerProgressbar(isDisplayed = false)
                        showTrailerRecyclerView(isDisplayed = true)
                        val trailers = dataState.data
                        trailerAdapter.differ.submitList(trailers)
                    }

                }
                is DataState.Error -> {
                    showTrailerProgressbar(isDisplayed = false)
                    showTrailerRecyclerView(isDisplayed = false)
                    Log.d(TAG, "error occurred .. ${dataState.exception.message}")
                }
            }
        })
    }

    private fun showTrailerProgressbar(isDisplayed: Boolean) {
        binding.progressBarTrailer.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun showTrailerRecyclerView(isDisplayed: Boolean) {
        binding.recyclerViewTrailer.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun observeCast() {
        viewModel.movieCast.observe(viewLifecycleOwner, { dataState ->

            when (dataState) {
                is DataState.Loading -> {
                    showCastRecyclerView(isDisplayed = false)
                    showCastProgressbar(isDisplayed = true)
                }
                is DataState.Success -> {
                    if (detailedMovie.id == checkedId) {
                        showCastProgressbar(isDisplayed = false)
                        showCastRecyclerView(isDisplayed = true)

                        val cast: List<CastModel> = dataState.data
                        castAdapter.differ.submitList(cast)
                    }
                }
                is DataState.Error -> {
                    showCastProgressbar(isDisplayed = false)
                    showCastRecyclerView(isDisplayed = false)
                    Log.d(TAG, "error occurred ${dataState.exception.message}")
                }
            }
        })
    }

    private fun showCastProgressbar(isDisplayed: Boolean) {
        binding.progressBarCast.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun showCastRecyclerView(isDisplayed: Boolean) {
        binding.recyclerViewCast.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun observeReviews() {
        viewModel.movieReviews.observe(viewLifecycleOwner, { dataState ->

            when (dataState) {
                is DataState.Loading -> {
                    showReviewRecyclerView(isDisplayed = false)
                    showReviewProgressbar(isDisplayed = true)
                }
                is DataState.Success -> {
                    if (detailedMovie.id == checkedId) {
                        showReviewProgressbar(isDisplayed = false)
                        showReviewRecyclerView(isDisplayed = true)

                        val reviews = dataState.data
                        if (reviews.isNullOrEmpty()) {
                            showReviewRecyclerView(isDisplayed = false)
                            showEmptyReviewText(isDisplayed = true)

                        } else {
                            showEmptyReviewText(isDisplayed = false)
                            reviewAdapter.differ.submitList(reviews)
                        }
                    }
                }
                is DataState.Error -> {
                    showReviewProgressbar(isDisplayed = false)
                    showReviewRecyclerView(isDisplayed = false)
                    Log.d(TAG, "error occurred ${dataState.exception.message}")
                }
            }
        })
    }

    private fun showReviewProgressbar(isDisplayed: Boolean) {
        binding.progressBarReview.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun showReviewRecyclerView(isDisplayed: Boolean) {
        binding.recyclerViewReviews.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun showEmptyReviewText(isDisplayed: Boolean) {
        binding.tvEmptyReview.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun initTrailerRecyclerView() {

        trailerAdapter = MovieTrailerAdapter(typeface, this)
        binding.recyclerViewTrailer.apply {
            adapter = trailerAdapter
            setHasFixedSize(true)
        }
    }

    private fun initCastRecyclerView() {
        castAdapter = MovieCastAdapter(typeface, this)
        binding.recyclerViewCast.apply {
            adapter = castAdapter
            setHasFixedSize(true)
        }
    }

    private fun initReviewRecyclerView() {
        reviewAdapter = MovieReviewAdapter(typeface)
        binding.recyclerViewReviews.apply {
            adapter = reviewAdapter
            setHasFixedSize(true)
        }
    }

    private fun populateUi(movie: MovieModel) {
        val date = '(' + movie.release_date + ')'
        binding.apply {
            Glide.with(binding.root)
                .load("${Constant.IMAGE_URL}${movie.poster_path}")
                .error(R.drawable.ic_baseline_image_24)
                .into(imageViewDetail)

            textViewTitleDetail.text = movie.title
            textViewDateDetail.text = date
            expand_text_view.text = movie.overview

            ivFav.setOnClickListener {
                toggleFavMovies(movie)
            }
        }
    }

    private fun applyCustomFont() {
        binding.apply {
            textViewReviewLabel.typeface = typeface
            textViewCastLabel.typeface = typeface
            textViewTrailerLabel.typeface = typeface
            textViewTitleDetail.typeface = typeface
            textViewDateDetail.typeface = typeface
            expandableText.typeface = typeface
        }
    }

    override fun onItemClick(trailerModel: TrailerModel?) {
        val intent = Intent(activity, YoutubeActivity::class.java)
        intent.putExtra(TRAILER, trailerModel)
        startActivity(intent)
    }

    override fun onItemClick(castModel: CastModel?) {
        val action =
            MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieCastFragment(castModel!!)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

