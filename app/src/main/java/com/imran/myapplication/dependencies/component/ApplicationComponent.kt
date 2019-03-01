package com.imran.myapplication.dependencies.component

import android.app.Application
import com.imran.myapplication.application.BaseApplication
import com.imran.myapplication.dependencies.activityBuilder.ActivityBuilder
import com.imran.myapplication.dependencies.modules.ApplicationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,ActivityBuilder::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    fun inject(myApplication: BaseApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}