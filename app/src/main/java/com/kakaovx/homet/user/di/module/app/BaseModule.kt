package com.kakaovx.homet.user.di.module.app

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.image.ImageFactory
import com.kakaovx.homet.user.component.member.MemberManager
import com.kakaovx.homet.user.component.preference.MemberPreference
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
    fun provideMemberManager(preference: MemberPreference): MemberManager = MemberManager(preference)

    @Provides
    @Singleton
    fun provideImageFactory(): ImageFactory = ImageFactory()

}