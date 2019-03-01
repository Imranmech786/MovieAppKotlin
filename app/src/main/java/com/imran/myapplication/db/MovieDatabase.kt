package com.imran.myapplication.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.imran.myapplication.model.Movie

@Database(entities = [Movie::class], version = 3, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDAO(): IMovieDAO
}