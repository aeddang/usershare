package com.kakaovx.homet.user.component.di.app

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(restApi: RestfulApi, setting: SettingPreference)
            = Repository(restApi, setting)

}