package com.kakaovx.homet.user.component.di.module.app

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.vxcore.VxCamera
import com.kakaovx.homet.user.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(executors: AppExecutors,
                          restApi: RestfulApi,
                          setting: SettingPreference,
                          camera: VxCamera): Repository
            = Repository(executors, restApi, setting, camera)
}