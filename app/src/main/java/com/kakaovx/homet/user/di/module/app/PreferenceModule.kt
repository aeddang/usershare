package com.kakaovx.homet.user.di.module.app

import android.app.Application
import com.kakaovx.homet.user.component.preference.AccountPreference
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule{

    @Provides
    @Singleton
    fun provideSettingPreference(application: Application): SettingPreference {
        return SettingPreference(application)
    }
    @Provides
    @Singleton
    fun provideAccountrPreference(application: Application):AccountPreference {
        return AccountPreference(application)
    }
}