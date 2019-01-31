package com.kakaovx.homet.user.component.di

import com.kakaovx.homet.user.component.di.annotation.ActivityScope
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.di.module.page.PageSplashModule
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.page.PageMain
import com.kakaovx.homet.user.ui.splash.SplashActivity
import com.kakaovx.homet.user.ui.page.PageSplash
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindSplashActivity(): SplashActivity

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageSplashModule::class
    ])
    internal abstract fun bindPageSplash(): PageSplash


    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun mainActivity(): MainActivity

    @PageScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindPageMain(): PageMain


}