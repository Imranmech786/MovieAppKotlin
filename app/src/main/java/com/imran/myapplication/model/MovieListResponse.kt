package com.imran.myapplication.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(@SerializedName("page") val page: Int, @SerializedName("results") val moviesResult: List<Movie>? )