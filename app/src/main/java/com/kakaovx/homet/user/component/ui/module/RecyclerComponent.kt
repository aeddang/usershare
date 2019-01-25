package com.kakaovx.homet.user.component.ui.module

import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.skeleton.view.BannerList
import com.kakaovx.homet.user.component.ui.skeleton.view.ComponentList
import dagger.Component

@ComponentScope
@Component( modules = [AdapterModule::class, LayoutManagerModule::class])
interface RecyclerComponent {
    fun adapterUtil(): AdapterUtil
    fun layoutManagerUtil(): LayoutManagerUtil

    fun inject(context: BannerList)
    fun inject(context: ComponentList)
}