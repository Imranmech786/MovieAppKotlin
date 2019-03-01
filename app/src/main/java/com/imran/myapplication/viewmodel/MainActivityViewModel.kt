package com.imran.myapplication.viewmodel

import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.navigator.MainActivityNavigator
import com.imran.myapplication.retrofit.APIInterface
import dagger.Module
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@Module
class MainActivityViewModel(
    apiInterface: APIInterface?,
    movieDatabase: MovieDatabase?,
    executor: Executor?,
    publishSubject: PublishSubject<String>?
) : BaseViewModel<MainActivityNavigator>(apiInterface, movieDatabase, executor, publishSubject) {

    val observable: Observable<String>
        get() = publishSubject!!.debounce(300, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .filter { s -> s.trim { it <= ' ' }.isNotEmpty() }
            .map { s -> s.trim { it <= ' ' } }.observeOn(AndroidSchedulers.mainThread())

    val observer: Observer<String>
        get() = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(strings: String) {
                navigator!!.searchQuery(strings.trim { it <= ' ' })
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }

    fun query(query: String) {
        publishSubject!!.onNext(query)
    }

}
