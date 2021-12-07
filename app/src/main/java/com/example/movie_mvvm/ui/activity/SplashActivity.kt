package com.example.movie_mvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movie_mvvm.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivSplash.alpha = 0F
        binding.ivSplash.animate().setDuration(1500).alpha(1F).withEndAction {
            val startMainActivityIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(startMainActivityIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}