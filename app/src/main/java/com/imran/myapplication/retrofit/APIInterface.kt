package com.imran.myapplication.retrofit

import com.imran.myapplication.model.Movie
import com.imran.myapplication.model.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {

    //Highest Rated
    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int

    ): Call<MovieListResponse>

    @GET("search/movie")
    fun getSearchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int

    ): Call<MovieListResponse>

    //Response to get movie details to display on Details UI
    //@Path sets value for {args}
    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<Movie>
}