package com.imran.myapplication.dependencies.modules

import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.retrofit.APIInterface
import com.imran.myapplication.viewmodel.FavouriteMovieViewModel
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class FavouriteMovieModule {

    @Provides
    fun favouriteMovieViewModel(
        apiInterface: APIInterface?,
        movieDatabase: MovieDatabase?,
        executor: Executor?
    ): FavouriteMovieViewModel {
        return FavouriteMovieViewModel(apiInterface, movieDatabase, executor)
    }
}
