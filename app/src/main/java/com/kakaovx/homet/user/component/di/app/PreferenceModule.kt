package com.kakaovx.homet.user.component.di.app

import android.app.Application
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule{

    @Provides
    @Singleton
    fun provideSettingPreference(app: Application): SettingPreference {
        return SettingPreference(app)
    }
}