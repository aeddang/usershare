package com.kakaovx.homet.user.component.di

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.component.annotation.ActivityScope
import com.kakaovx.homet.user.component.annotation.PageScope
import com.kakaovx.homet.user.component.di.page.ApiModule
import com.kakaovx.homet.user.component.di.page.*
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.page.PageMain
import com.kakaovx.homet.user.ui.page.PageViewPager
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun mainActivity(): MainActivity

    @PageScope
    @ContributesAndroidInjector(modules = [
        FragmentModule::class,
        ApiModule::class
    ])
    internal abstract fun pageMain(): PageMain

    @PageScope
    @ContributesAndroidInjector(modules = [
        FragmentModule::class,
        ApiModule::class,
        AdapterModule::class,
        LayoutManagerModule::class,
        PagePagerAdapterModule::class,
        ViewPagerAdapterModule::class
    ])
    internal abstract fun pageViewPager(): PageViewPager
}