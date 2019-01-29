package com.kakaovx.homet.user.component.di.component

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class
])

interface AppComponent {
    fun context(): Context
    fun application(): Application
}