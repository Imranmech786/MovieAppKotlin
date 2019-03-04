package com.imran.myapplication.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.model.Movie
import com.imran.myapplication.retrofit.APIInterface
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor

abstract class BaseViewModel<N> : ViewModel {

    var navigator: N? = null
    var apiInterface: APIInterface? = null

    var movieDatabase: MovieDatabase? = null

    var executor: Executor? = null

    var publishSubject: PublishSubject<String>? = null

    val isAddedToFavourite = MutableLiveData<Boolean>()


    constructor(
        apiInterface: APIInterface?, movieDatabase: MovieDatabase?,
        executor: Executor?
    ) {
        this.apiInterface = apiInterface
        this.movieDatabase = movieDatabase
        this.executor = executor
    }

    constructor(
        apiInterface: APIInterface?,
        movieDatabase: MovieDatabase?,
        executor: Executor?,
        publishSubject: PublishSubject<String>?
    ) {
        this.apiInterface = apiInterface
        this.movieDatabase = movieDatabase
        this.executor = executor
        this.publishSubject = publishSubject
    }


    fun handleFavourites(movie: Movie) {
        executor?.execute(Runnable {
            if (movieDatabase?.movieDAO()?.loadMovie(movie.movieTitle!!) == null) {
                movie.isFavourite=(true)
                movieDatabase!!.movieDAO().saveMovieAsFavourite(movie)
                isAddedToFavourite.postValue(true)
            } else {
                movie.isFavourite=(false)
                movieDatabase!!.movieDAO().removeMovieFromFavourites(movie)
                isAddedToFavourite.postValue(false)
            }
        })
    }
}
