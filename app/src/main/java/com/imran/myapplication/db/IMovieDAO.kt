package com.imran.myapplication.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.imran.myapplication.model.Movie

@Dao
interface IMovieDAO {

    @Query("SELECT * FROM favourite_movie")
    fun loadAllMovies(): List<Movie>

    @Insert
    fun saveMovieAsFavourite(movie: Movie)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(movie: Movie)

    @Delete
    fun removeMovieFromFavourites(movie: Movie)

    @Query("SELECT * FROM favourite_movie WHERE movieId = :id")
    fun loadMovieById(id: Int): LiveData<Movie>

    @Query("SELECT * FROM favourite_movie WHERE favourite = 1 AND movieId = :id")
    fun getFavourite(id: Int): LiveData<Movie>

    @Query("SELECT * FROM favourite_movie WHERE movie_title = :movieTitle")
    fun loadMovie(movieTitle: String): Movie
}