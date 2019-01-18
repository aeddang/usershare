package com.kakaovx.homet.component.app

import com.kakaovx.homet.component.network.NetworkModule
import com.kakaovx.homet.component.preference.PreferenceModule
import com.kakaovx.homet.component.preference.SettingPreference
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