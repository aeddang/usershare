package com.kakaovx.homet.user.component.di.module.app
import android.app.Application
import com.kakaovx.homet.user.App
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    internal abstract fun application(application: App): Application
}