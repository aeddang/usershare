package com.kakaovx.homet.user.component.di

import com.kakaovx.homet.user.component.di.annotation.ActivityScope
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.di.page.*
import com.kakaovx.homet.user.component.di.page.viewmodel.SplashFragmentModule
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.page.PageMain
import com.kakaovx.homet.user.ui.page.PageViewPager
import com.kakaovx.homet.user.ui.splash.SplashActivity
import com.kakaovx.homet.user.ui.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindSplashActivity(): SplashActivity

    @PageScope
    @ContributesAndroidInjector(modules = [
        FragmentModule::class,
        SplashFragmentModule::class
    ])
    internal abstract fun bindSplashFragment(): SplashFragment

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun mainActivity(): MainActivity

    @PageScope
    @ContributesAndroidInjector(modules = [
        FragmentModule::class
    ])
    internal abstract fun pageMain(): PageMain

    @PageScope
    @ContributesAndroidInjector(modules = [
        FragmentModule::class,
        AdapterModule::class,
        LayoutManagerModule::class,
        PagePagerAdapterModule::class,
        ViewPagerAdapterModule::class
    ])
    internal abstract fun pageViewPager(): PageViewPager
}