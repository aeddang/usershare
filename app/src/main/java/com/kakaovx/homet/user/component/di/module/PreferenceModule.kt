package com.kakaovx.homet.user.component.di.module

import android.app.Application
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Module
import dagger.Provides

@Module
class PreferenceModule {

    @Provides
    @PageScope
    fun provideSettingPreference(application: Application): SettingPreference {
        return SettingPreference(application)
    }
}