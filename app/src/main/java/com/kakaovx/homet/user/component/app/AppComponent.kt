package com.kakaovx.homet.user.component.app

import com.kakaovx.homet.user.component.network.module.NetworkModule
import com.kakaovx.homet.user.component.preference.PreferenceModule
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    PreferenceModule::class,
    NetworkModule::class
])

interface AppComponent {
    fun retrofit(): Retrofit
    fun setting(): SettingPreference
}