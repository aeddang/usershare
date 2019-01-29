package com.kakaovx.homet.user.component.di.component

import android.app.Application
import com.kakaovx.homet.user.App
import com.kakaovx.homet.user.component.di.binder.AppBinder
import com.kakaovx.homet.user.component.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    PreferenceModule::class,
    AppBinder::class,
    AndroidSupportInjectionModule::class
])

interface AppComponent: AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}