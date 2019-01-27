package com.kakaovx.homet.user.component.ui.component

import com.kakaovx.homet.user.component.ui.view.viewpager.BannerViewPager
import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.module.PagerAdapterModule
import com.kakaovx.homet.user.component.ui.module.PagerAdapterUtil
import dagger.Component

@ComponentScope
@Component( modules = [PagerAdapterModule::class])
interface ViewPagerComponent {
    fun adapterUtil(): PagerAdapterUtil
    fun inject(context: BannerViewPager)
}