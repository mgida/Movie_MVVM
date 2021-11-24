package com.example.movie_mvvm.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_mvvm.R
import com.example.movie_mvvm.adapter.FavMovieAdapter
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.FragmentMovieFavouriteBinding
import com.example.movie_mvvm.ui.MainActivity
import com.example.movie_mvvm.utils.Constant
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

class MovieFavouriteFragment : Fragment(R.layout.fragment_movie_favourite),
    FavMovieAdapter.OnItemClickListener {

    private var _binding: FragmentMovieFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieViewModel
    private lateinit var typeface: Typeface
    private lateinit var favMovieAdapter: FavMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieFavouriteBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel
        typeface = Typeface.createFromAsset(requireActivity().assets, Constant.AntiqueFont)
        favMovieAdapter = FavMovieAdapter(typeface, this)

        initFavRecyclerView()

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

                Snackbar.make(view, "deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {

                        viewModel.insertMovie(swipedMovie)
                        Toast.makeText(requireActivity(), "Saved Successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerViewMovieFav)
        }

        observeFavourites()
    }

    private fun observeFavourites() {
        viewModel.favourites.observe(viewLifecycleOwner) { favMovies ->
            favMovies?.let {
                favMovieAdapter.differ.submitList(it)
            }
        }
    }

    private fun initFavRecyclerView() {
        binding.recyclerViewMovieFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favMovieAdapter
            setHasFixedSize(true)
        }
    }

    override fun onItemClick(movie: MovieModel?) {
        val action =
            MovieFavouriteFragmentDirections.actionMovieFavouriteFragmentToMovieDetailFragment(movie!!)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

