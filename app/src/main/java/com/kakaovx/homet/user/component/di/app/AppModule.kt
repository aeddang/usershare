package com.kakaovx.homet.user.component.di.app
import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.App
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class AppModule {

    @Binds
    internal abstract fun application(app: App): Application
}