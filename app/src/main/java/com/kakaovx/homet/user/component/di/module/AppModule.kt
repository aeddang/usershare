package com.kakaovx.homet.user.component.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApp(): Application
            = app

    @Provides
    @Singleton
    fun provideContext(): Context
            = app.applicationContext
}