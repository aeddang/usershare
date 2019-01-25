package com.kakaovx.homet.component.ui.component

import com.kakaovx.homet.component.annotation.ComponentScope
import com.kakaovx.homet.component.ui.module.AdapterModule
import com.kakaovx.homet.component.ui.module.AdapterUtil
import com.kakaovx.homet.component.ui.module.LayoutManagerModule
import com.kakaovx.homet.component.ui.module.LayoutManagerUtil
import com.kakaovx.homet.component.ui.view.BannerList
import com.kakaovx.homet.component.ui.view.ComponentList
import dagger.Component

@ComponentScope
@Component( modules = [AdapterModule::class, LayoutManagerModule::class])
interface RecyclerComponent {
    fun adapterUtil(): AdapterUtil
    fun layoutManagerUtil(): LayoutManagerUtil

    fun inject(context: BannerList)
    fun inject(context: ComponentList)
}