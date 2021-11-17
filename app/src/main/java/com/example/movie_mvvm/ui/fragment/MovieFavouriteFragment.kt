package com.example.movie_mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.FavMovieAdapter
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.FragmentMovieFavouriteBinding
import com.example.movie_mvvm.ui.MainActivity
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

class MovieFavouriteFragment : Fragment(R.layout.fragment_movie_favourite),
    FavMovieAdapter.OnItemClickListener {

    private var _binding: FragmentMovieFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieViewModel


    //  private lateinit var favMovieAdapter: FavMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieFavouriteBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel

        // initRecyclerViewFav()

        val favMovieAdapter = FavMovieAdapter(this)
        binding.recyclerViewMovieFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favMovieAdapter
            setHasFixedSize(true)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition

                val swipedMovie = favMovieAdapter.differ.currentList[position]

                viewModel.deleteMovie(swipedMovie)

                Snackbar.make(view, "Successfully deleted movie", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.insertMovie(swipedMovie)

                        Toast.makeText(requireActivity(), "inserted", Toast.LENGTH_SHORT)
                            .show()
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerViewMovieFav)
        }

        //getAllEntries()
        // observeFavMovies()

        viewModel.favourites.observe(viewLifecycleOwner) { favMovies ->

            favMovies?.let {
                favMovieAdapter.differ.submitList(it)
            }

        }

//    private fun getAllEntries() {
//        viewModel.favouritess.observe(viewLifecycleOwner) {
//
//            it?.let {
//                favMovieAdapter.differ.submitList(it)
//            }
////            if (it == null || it.isEmpty()) {
////                 showFavProgressbar(true)
////                showFavRecyclerView(false)
////                binding.textViewNoResultMovieFav.visibility = View.VISIBLE
////            } else {
////                showFavProgressbar(false)
////                binding.textViewNoResultMovieFav.visibility = View.GONE
////
////                showFavRecyclerView(true)
////
////            }
//
//
//        }
//    }

//    private fun observeFavMovies() {
//
//        viewModel.favourites.observe(viewLifecycleOwner, {
//            if (it != null && it.isNotEmpty()) {
//                favMovieAdapter.differ.submitList(it)
//
//            }
//        })
//
////        viewModel.favMovies.observe(viewLifecycleOwner, { dataState ->
////
////
////            when (dataState) {
////                is DataState.Loading -> {
////
////                    showFavRecyclerView(isDisplayed = false)
////                    showFavProgressbar(isDisplayed = true)
////
////                }
////                is DataState.Success -> {
////
////                    showFavProgressbar(isDisplayed = false)
////                    showFavRecyclerView(isDisplayed = true)
////
////                    val favMovies = dataState.data
////                    favMovieAdapter.differ.submitList(favMovies)
////                    Toast.makeText(requireActivity(), "${favMovies.size}", Toast.LENGTH_SHORT)
////                        .show()
////                }
////                is DataState.Error -> {
////                    showFavProgressbar(isDisplayed = false)
////                    showFavRecyclerView(isDisplayed = false)
////                    Log.d(Constant.TAG, "error occurred ${dataState.exception.message}")
////                }
////            }
//
//        //     })
//
    }

//    private fun showFavProgressbar(isDisplayed: Boolean) {
//        binding.progressBarMovieFav.visibility = if (isDisplayed) View.VISIBLE else View.GONE
//
//    }
//
//    private fun showFavRecyclerView(isDisplayed: Boolean) {
//        binding.recyclerViewMovieFav.visibility = if (isDisplayed) View.VISIBLE else View.GONE
//    }


//    private fun initRecyclerViewFav() {
//        favMovieAdapter = FavMovieAdapter(this)
//        binding.recyclerViewMovieFav.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = favMovieAdapter
//            setHasFixedSize(true)
//        }
//    }

    override fun onItemClick(movie: MovieModel?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}

