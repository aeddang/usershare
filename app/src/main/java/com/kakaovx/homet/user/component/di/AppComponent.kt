package com.kakaovx.homet.user.component.di

import com.kakaovx.homet.user.App
import com.kakaovx.homet.user.component.di.app.AppModule
import com.kakaovx.homet.user.component.di.app.NetworkModule
import com.kakaovx.homet.user.component.di.app.PreferenceModule
import com.kakaovx.homet.user.component.di.app.RepositoryModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class,
    AndroidBindingModule::class,
    PreferenceModule::class,
    NetworkModule::class,
    AndroidSupportInjectionModule::class
])

interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
