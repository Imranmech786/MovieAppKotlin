package com.imran.myapplication.dependencies.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.imran.myapplication.db.MovieDatabase
import com.imran.myapplication.retrofit.APIInterface
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {

    val MOVIE_DB_URL = "http://api.themoviedb.org/3/"
    val DATABASE_NAME = "movieList"

    @Provides
    @Named("ApplicationContext")
    @Singleton
    fun provideContext(application: Application): Application {
        return application;
    }

    @Provides
    @Named("DatabaseInfo")
    @Singleton
    fun getDbName(): String {
        return DATABASE_NAME;
    }

    @Provides
    @Singleton
    fun movieDatabase(
        @Named("ApplicationContext") context: Application,
        @Named("DatabaseInfo") dbNAme: String
    ): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, dbNAme)
            .fallbackToDestructiveMigration().build()

    }

    @Provides
    @Singleton
    fun publishSubject(): PublishSubject<String> {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    fun getApiInterface(retroFit: Retrofit): APIInterface {
        return retroFit.create<APIInterface>(APIInterface::class.java)
    }

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MOVIE_DB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun getOkHttpCleint(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

}