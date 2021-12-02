package com.example.movie_mvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movie_mvvm.data.model.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}


