package com.imran.myapplication.navigator

import android.view.View

import com.imran.myapplication.model.Movie

interface BaseNavigator {

    fun onItemClick(view: View, movie: Movie)

}
