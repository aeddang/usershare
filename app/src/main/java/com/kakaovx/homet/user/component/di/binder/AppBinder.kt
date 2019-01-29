package com.kakaovx.homet.user.component.di.binder

import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.di.module.ApiModule
import com.kakaovx.homet.user.component.di.module.ViewModelModule
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.page.*
import com.kakaovx.homet.user.ui.splash.SplashActivity
import com.kakaovx.homet.user.ui.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppBinder {

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindPageMain(): PageMain

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindPageNetwork(): PageNetworkTest

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindPageSub(): PageSub

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindPageViewPager(): PageViewPager

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    @PageScope
    abstract fun bindPopupTest(): PopupTest

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [ApiModule::class])
    @PageScope
    abstract fun bindSplashFragment(): SplashFragment

}