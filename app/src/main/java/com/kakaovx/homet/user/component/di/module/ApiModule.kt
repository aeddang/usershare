package com.kakaovx.homet.user.component.di.module

import android.content.Context
import com.kakaovx.homet.user.component.api.Api
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Module
import dagger.Provides

@Module
class ApiModule {

    @Provides
    @PageScope
    fun provideRepository(ctx: Context, restApi: RestfulApi, setting: SettingPreference)
            = Api(ctx, restApi, setting)
}