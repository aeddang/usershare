package com.kakaovx.homet.user.component.di.app

import android.app.Application
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideSettingPreference(): SettingPreference {
        return SettingPreference(app)
    }

}