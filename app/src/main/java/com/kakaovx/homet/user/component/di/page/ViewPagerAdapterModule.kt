package com.kakaovx.homet.user.component.di.page
import com.kakaovx.homet.user.component.ui.module.BannerPagerAdapter


import dagger.Module
import dagger.Provides

@Module
class ViewPagerAdapterModule {

    @Provides
    fun provideBannerPagerAdapter(): BannerPagerAdapter {
        return BannerPagerAdapter()
    }
}




