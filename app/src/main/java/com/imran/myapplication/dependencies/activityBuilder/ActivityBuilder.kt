package com.imran.myapplication.dependencies.activityBuilder

import com.imran.myapplication.activity.DetailActivity
import com.imran.myapplication.activity.MainActivity
import com.imran.myapplication.dependencies.modules.FavouriteMovieModule
import com.imran.myapplication.dependencies.modules.MainActivityModule
import com.imran.myapplication.dependencies.modules.MovieDetailModule
import com.imran.myapplication.dependencies.modules.TopRatedMovieModule
import com.imran.myapplication.fragment.FavouriteMovieFragment
import com.imran.myapplication.fragment.TopRatedMovieFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [TopRatedMovieModule::class])
    abstract fun topRatedMovieFragment(): TopRatedMovieFragment

    @ContributesAndroidInjector(modules = [FavouriteMovieModule::class])
    internal abstract fun favouriteMovieFragment(): FavouriteMovieFragment

    @ContributesAndroidInjector(modules = [MovieDetailModule::class])
    internal abstract fun detailActivity(): DetailActivity
}