package com.imran.myapplication.viewmodel

import android.arch.lifecycle.ViewModel
import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.retrofit.APIInterface
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor

/**
 * Created by kautilya on 01/02/18.
 */

abstract class BaseViewModel<N> : ViewModel {

    var navigator: N? = null
    var apiInterface: APIInterface? = null

    var movieDatabase: MovieDatabase? = null

    var executor: Executor? = null

    var publishSubject: PublishSubject<String>? = null

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
}
