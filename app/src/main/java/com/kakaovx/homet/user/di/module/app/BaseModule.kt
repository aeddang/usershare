package com.kakaovx.homet.user.di.module.app

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.account.AccountManager
import com.kakaovx.homet.user.component.image.ImageFactory
import com.kakaovx.homet.user.component.preference.AccountPreference
import com.kakaovx.homet.user.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class BaseModule {

    @Provides
    @Named("appContext")
    fun provideContext(app: Application): Context
            = app.applicationContext

    @Provides
    fun provideExecutor(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideAccountManager(preference: AccountPreference): AccountManager = AccountManager(preference)

    @Provides
    @Singleton
    fun provideImageFactory(): ImageFactory = ImageFactory()

}