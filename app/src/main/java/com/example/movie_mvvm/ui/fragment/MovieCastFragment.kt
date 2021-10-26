package com.example.movie_mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.model.cast.CastModel
import com.example.movie_mvvm.databinding.FragmentMovieCastBinding
import com.example.movie_mvvm.utils.Constant.Companion.IMAGE_URL

class MovieCastFragment : Fragment(R.layout.fragment_movie_cast) {

    private var _binding: FragmentMovieCastBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<MovieCastFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieCastBinding.bind(view)
        val cast = args.cast
        populateUi(cast)

    }

    private fun populateUi(cast: CastModel) {
        binding.apply {
            Glide.with(this@MovieCastFragment)
                .load("$IMAGE_URL${cast.profile_path}")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageViewCast)

            textViewCastName.text = cast.original_name
            textViewCharacter.text = cast.character
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

