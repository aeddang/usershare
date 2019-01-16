package com.kakaovx.homet.component.preference

import android.app.Application
import com.kakaovx.homet.App
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