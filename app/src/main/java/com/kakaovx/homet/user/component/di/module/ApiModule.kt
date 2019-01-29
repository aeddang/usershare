package com.kakaovx.homet.user.component.di.module

import com.kakaovx.homet.user.component.api.Api
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.viewmodel.ApiModelFactory
import com.kakaovx.homet.user.component.preference.SettingPreference
import dagger.Module
import dagger.Provides

@Module
class ApiModule {

    @Provides
    @PageScope
    fun provideRepository(restApi: RestfulApi, setting: SettingPreference, apiFactory: ApiModelFactory)
            = Api(restApi, setting, apiFactory)
}