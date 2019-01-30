package com.kakaovx.homet.user.component.di.page

import android.support.v4.app.Fragment
import com.kakaovx.homet.user.component.ui.module.PagePagerAdapter

import dagger.Module
import dagger.Provides

@Module
class PagePagerAdapterModule {

    @Provides
    fun providePagePagerAdapter(fragment: Fragment): PagePagerAdapter {
        return PagePagerAdapter(fragment.fragmentManager)
    }

}



