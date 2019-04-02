package com.kakaovx.homet.user.di.module.app

import android.app.Activity
import android.app.Application
import com.kakaovx.homet.user.component.preference.MemberPreference
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
    fun provideMemberPreference(application: Application):MemberPreference {
        return MemberPreference(application)
    }
}