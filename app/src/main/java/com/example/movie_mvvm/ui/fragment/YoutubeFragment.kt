package com.example.movie_mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.movie_mvvm.R
import com.example.movie_mvvm.databinding.FragmentYoutubeBinding
import com.example.movie_mvvm.utils.Constant.Companion.YOUTUBE_API_KEY
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

//class YoutubeFragment : Fragment(R.layout.fragment_youtube) {
//    private var _binding: FragmentYoutubeBinding? = null
//    private val binding get() = _binding!!
//    private val args by navArgs<YoutubeFragmentArgs>()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        _binding = FragmentYoutubeBinding.bind(view)
//
//        val trailer = args.trailer
//
//        val movieYoutubeKey = trailer.key
//
//
//
//
//        binding.ytPlayer.initialize(
//            YOUTUBE_API_KEY,
//            object : YouTubePlayer.OnInitializedListener {
//                override fun onInitializationSuccess(
//                    provider: YouTubePlayer.Provider?,
//                    player: YouTubePlayer?,
//                    p2: Boolean
//                ) {
//                    player?.loadVideo(movieYoutubeKey)
//                    player?.play()
//                }
//
//                override fun onInitializationFailure(
//                    provider: YouTubePlayer.Provider?,
//                    player: YouTubeInitializationResult?
//                ) {
//                    Toast.makeText(activity, "Video player Failed", Toast.LENGTH_SHORT).show()
//
//                }
//            }
//        )
//
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}