package com.kakaovx.homet.user.component.ui.module

import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.BannerList
import dagger.Component

@ComponentScope
@Component( modules = [AdapterModule::class, LayoutManagerModule::class])
interface ListComponent {
    fun inject(context: BannerList)
}