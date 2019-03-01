package com.imran.myapplication.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.imran.myapplication.BuildConfig
import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.model.Movie
import com.imran.myapplication.model.MovieListResponse
import com.imran.myapplication.navigator.BaseNavigator
import com.imran.myapplication.retrofit.APIInterface
import com.imran.myapplication.utils.StateLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class TopRatedMovieViewModel(apiInterface: APIInterface?, movieDatabase: MovieDatabase?, executor: Executor?) :
    BaseViewModel<BaseNavigator>(apiInterface, movieDatabase, executor) {

    val isPaginationLoadingLiveData = MutableLiveData<Boolean>()
    val listMutableLiveData = StateLiveData<List<Movie>>()
    val isAddedToFavourite = MutableLiveData<Boolean>()

    fun loadTopRatedMovie(mPageNumber: Int) {
        setLoadingLiveData(mPageNumber)
        apiInterface!!.getTopRatedMovies(BuildConfig.API_KEY, "en", mPageNumber)
            .enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    removeLoadingLiveData(mPageNumber, null)
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body()!!.moviesResult != null && response.body()!!.moviesResult?.isNotEmpty()!!) {
                            val movieListResponse = response.body()
                            listMutableLiveData.postSuccess(movieListResponse!!.moviesResult!!)
                        } else {
                            listMutableLiveData.postComplete()
                        }
                    } else {
                        listMutableLiveData.postComplete()
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    removeLoadingLiveData(mPageNumber, t)
                }
            })
    }

    fun handleFavourites(movie: Movie) {
        executor?.execute {
            if (movieDatabase?.movieDAO()?.loadMovie(movie.movieTitle!!) == null) {
                movie.isFavourite = (true)
                movieDatabase!!.movieDAO().saveMovieAsFavourite(movie)
                isAddedToFavourite.postValue(true)
            } else {
                movie.isFavourite = (false)
                movieDatabase!!.movieDAO().removeMovieFromFavourites(movie)
                isAddedToFavourite.postValue(false)
            }
        }
    }

    fun setLoadingLiveData(mPageNumber: Int) {
        if (mPageNumber > 1) {
            isPaginationLoadingLiveData.setValue(true)
        } else {
            listMutableLiveData.postLoading()
        }
    }

    fun removeLoadingLiveData(mPageNumber: Int, throwable: Throwable?) {
        if (mPageNumber > 1) {
            isPaginationLoadingLiveData.setValue(false)
        } else if (throwable != null) {
            listMutableLiveData.postError(throwable)
        }
    }

    fun getSearchMovie(query: String, mPageNumber: Int) {
        setLoadingLiveData(mPageNumber)
        apiInterface!!.getSearchMovies(BuildConfig.API_KEY, query, mPageNumber)
            .enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    removeLoadingLiveData(mPageNumber, null)
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body()!!.moviesResult != null && response.body()!!.moviesResult?.isNotEmpty()!!) {
                            val movieListResponse = response.body()
                            listMutableLiveData.postSuccess(movieListResponse!!.moviesResult!!)
                        } else {
                            listMutableLiveData.postComplete()
                        }
                    } else {
                        listMutableLiveData.postComplete()
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    removeLoadingLiveData(mPageNumber, t)
                    Log.i("api", t.message)
                }
            })
    }
}
