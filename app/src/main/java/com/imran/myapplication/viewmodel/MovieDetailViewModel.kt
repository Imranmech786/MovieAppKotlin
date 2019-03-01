package com.imran.myapplication.viewmodel

import com.imran.myapplication.BuildConfig
import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.model.Movie
import com.imran.myapplication.navigator.BaseNavigator
import com.imran.myapplication.retrofit.APIInterface
import com.imran.myapplication.utils.StateLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class MovieDetailViewModel(apiInterface: APIInterface?, movieDatabase: MovieDatabase?, executor: Executor?) :
    BaseViewModel<BaseNavigator>(apiInterface, movieDatabase, executor) {

    val listMutableLiveData: StateLiveData<Movie> = StateLiveData()

    fun loadMovieDetail(movie_id: Int) {
        listMutableLiveData.postLoading()
        apiInterface?.getMovie(movie_id, BuildConfig.API_KEY, "en")
            ?.enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    if (response.isSuccessful() && response.body() != null) {
                        val movie = response.body()
                        listMutableLiveData.postSuccess(movie!!)
                    } else {
                        listMutableLiveData.postComplete()
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    listMutableLiveData.postError(t)
                }
            })
    }
}
