package com.example.movie_mvvm.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.model.cast.CastModel
import com.example.movie_mvvm.databinding.FragmentMovieCastBinding
import com.example.movie_mvvm.utils.Constant.Companion.AntiqueFont
import com.example.movie_mvvm.utils.Constant.Companion.IMAGE_URL
import com.example.movie_mvvm.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieCastFragment : Fragment(R.layout.fragment_movie_cast) {

    private var _binding: FragmentMovieCastBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<MovieCastFragmentArgs>()
    private lateinit var typeface: Typeface
    private val viewModel by viewModels<MovieViewModel>()
    private var idObserved = MutableLiveData<Int>()
    private var checkedId: Int? = null
    private lateinit var cast: CastModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMovieCastBinding.bind(view)
        typeface = Typeface.createFromAsset(requireActivity().assets, AntiqueFont)

        cast = args.cast
        idObserved.postValue(cast.id)
        idObserved.observe(viewLifecycleOwner, {
            checkedId = it
        })

        viewModel.getCastDetail(personId = cast.id)
        populateUi(cast)
        observeCast()
    }

    private fun observeCast() {
        viewModel.castDetail.observe(viewLifecycleOwner, {
            if (cast.id == checkedId) {
                it?.let {
                    binding.apply {
                        textViewCastBirthday.typeface = typeface
                        textViewCastBirthday.text = it.birthday
                        textViewCastBio.typeface = typeface
                        textViewCastBio.text = it.biography
                    }
                }
            }

        })
    }

    private fun populateUi(cast: CastModel) {
        binding.apply {
            Glide.with(this@MovieCastFragment)
                .load("$IMAGE_URL${cast.profile_path}")
                .error(R.drawable.ic_baseline_image_24)
                .into(imageViewCast)

            textViewCastName.apply {
                typeface = typeface
                text = cast.original_name
            }
            textViewCharacter.apply {
                typeface = typeface
                text = cast.character
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}
