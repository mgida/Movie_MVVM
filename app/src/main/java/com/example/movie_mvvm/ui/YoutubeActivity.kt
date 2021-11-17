package com.example.movie_mvvm.ui

import android.os.Bundle
import android.widget.Toast
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.model.trailer.TrailerModel
import com.example.movie_mvvm.utils.Constant.Companion.YOUTUBE_API_KEY
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class YoutubeActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val intent = intent.getParcelableExtra<TrailerModel>("trailerModel")
        val yKey = intent?.key

        val ytPlayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)

        ytPlayer.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    player?.loadVideo(yKey)
                }
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@YoutubeActivity, "Video player Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}

