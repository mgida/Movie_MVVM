package com.example.movie_mvvm.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movie_mvvm.data.model.MovieModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieModel: MovieModel): Long

    @Delete
    suspend fun deleteMovie(movieModel: MovieModel)

    @Query("SELECT * FROM movies")
    fun getMoviesFromDB(): Flow<List<MovieModel>>

    @Query("select * from movies where ID = :id")
    fun selectMovieByID(id: Int): LiveData<MovieModel>


}