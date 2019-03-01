package com.imran.myapplication.dependencies.modules

import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.retrofit.APIInterface
import com.imran.myapplication.viewmodel.TopRatedMovieViewModel
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class TopRatedMovieModule {
    @Provides
    fun topRatedMovieViewModel(
        apiInterface: APIInterface,
        movieDatabase: MovieDatabase,
        executor: Executor
    ): TopRatedMovieViewModel {
        return TopRatedMovieViewModel(apiInterface, movieDatabase, executor)
    }
}
