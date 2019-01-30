package com.kakaovx.homet.user.component.di.page

import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.ui.module.BannerAdapter
import com.kakaovx.homet.user.component.ui.module.ComponentAdapter

import dagger.Module
import dagger.Provides

@Module
class AdapterModule {

    @Provides
    @PageScope
    fun provideBannerAdapter(): BannerAdapter {
        return BannerAdapter()
    }

    @Provides
    @PageScope
    fun provideComponentAdapter(): ComponentAdapter {
        return ComponentAdapter()
    }
}



