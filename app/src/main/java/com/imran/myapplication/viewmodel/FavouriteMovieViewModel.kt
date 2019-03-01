package com.imran.myapplication.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.model.Movie
import com.imran.myapplication.navigator.BaseNavigator
import com.imran.myapplication.retrofit.APIInterface
import java.util.concurrent.Executor

class FavouriteMovieViewModel(apiInterface: APIInterface?, movieDatabase: MovieDatabase?, executor: Executor?) :
    BaseViewModel<BaseNavigator>(apiInterface, movieDatabase, executor) {

    val listMutableLiveData = MutableLiveData<List<Movie>>()

    fun getFavMovieList() {
        executor?.execute(Runnable { listMutableLiveData.postValue(movieDatabase?.movieDAO()?.loadAllMovies()) })
    }

}
