package com.imran.myapplication.dependencies.modules

import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.retrofit.APIInterface
import com.imran.myapplication.viewmodel.MainActivityViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor

@Module
class MainActivityModule {
    @Provides
    fun mainActivityViewModel(
        apiInterface: APIInterface,
        movieDatabase: MovieDatabase,
        executor: Executor,
        publishSubject: PublishSubject<String>
    ): MainActivityViewModel {
        return MainActivityViewModel(apiInterface, movieDatabase, executor, publishSubject)
    }
}
