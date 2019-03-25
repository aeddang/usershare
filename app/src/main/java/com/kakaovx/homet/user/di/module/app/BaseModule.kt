package com.kakaovx.homet.user.di.module.app

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class BaseModule {

    @Provides
    @Named("appContext")
    fun provideContext(app: Application): Context
            = app.applicationContext

    @Provides
    fun provideExecutor(): AppExecutors = AppExecutors()

}