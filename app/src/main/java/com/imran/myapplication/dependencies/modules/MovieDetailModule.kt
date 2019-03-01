package com.imran.myapplication.dependencies.modules

import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.retrofit.APIInterface
import com.imran.myapplication.viewmodel.MovieDetailViewModel
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class MovieDetailModule {

    @Provides
    fun movieDetailViewModel(
        apiInterface: APIInterface?,
        movieDatabase: MovieDatabase?,
        executor: Executor?
    ): MovieDetailViewModel {
        return MovieDetailViewModel(apiInterface, movieDatabase, executor)
    }
}
