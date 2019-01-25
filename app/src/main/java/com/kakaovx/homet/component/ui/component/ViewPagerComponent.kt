package com.kakaovx.homet.component.ui.component

import com.kakaovx.homet.component.annotation.ComponentScope
import com.kakaovx.homet.component.ui.module.*
import com.kakaovx.homet.component.ui.view.BannerViewPager
import dagger.Component

@ComponentScope
@Component( modules = [PagerAdapterModule::class])
interface ViewPagerComponent {
    fun adapterUtil(): PagerAdapterUtil
    fun inject(context:BannerViewPager)
}