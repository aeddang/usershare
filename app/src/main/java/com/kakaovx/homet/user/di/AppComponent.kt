package com.kakaovx.homet.user.di

import com.kakaovx.homet.user.App
import com.kakaovx.homet.user.di.module.app.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    BaseModule::class,
    RepositoryModule::class,
    AndroidBindingModule::class,
    PreferenceModule::class,
    NetworkModule::class,
    ImageModule::class,
    CaptureModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}