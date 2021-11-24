package com.example.movie_mvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.database.MovieDatabase
import com.example.movie_mvvm.databinding.ActivityMainBinding
import com.example.movie_mvvm.repository.MovieRepository
import com.example.movie_mvvm.viewmodel.MovieViewModel
import com.example.movie_mvvm.viewmodel.MovieViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MovieViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController)


        val movieDao = MovieDatabase.invoke(this).getMovieDao()
        val movieRepository = MovieRepository(movieDao)
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}