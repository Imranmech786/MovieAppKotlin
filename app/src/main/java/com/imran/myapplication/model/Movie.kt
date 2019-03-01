package com.imran.myapplication.model


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.SerializedName


@Entity(tableName = "favourite_movie")
data class Movie(
    @ColumnInfo(name = "movie_title")
    @SerializedName("original_title")
    var movieTitle: String?,
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var movieOverview: String?,
    @ColumnInfo(name = "release")
    @SerializedName("release_date")
    var movieReleaseDate: String?,
    @ColumnInfo(name = "poster")
    @SerializedName("poster_path")
    var moviePoster: String?,
    @ColumnInfo(name = "voter_average")
    @SerializedName("vote_average")
    var voterAverage: Float?,
    @ColumnInfo(name = "backdrop")
    @SerializedName("backdrop_path")
    var backdrop: String?,
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var movieId: Int,
    @ColumnInfo(name = "favourite")
    var isFavourite: Boolean
)
