package com.kakaovx.homet.user.component.ui.component

import com.kakaovx.homet.user.component.ui.view.recycler.BannerRecycler
import com.kakaovx.homet.user.component.ui.view.recycler.ComponentRecycler
import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.module.AdapterModule
import com.kakaovx.homet.user.component.ui.module.AdapterUtil
import com.kakaovx.homet.user.component.ui.module.LayoutManagerModule
import com.kakaovx.homet.user.component.ui.module.LayoutManagerUtil
import dagger.Component

@ComponentScope
@Component( modules = [AdapterModule::class, LayoutManagerModule::class])
interface RecyclerComponent {
    fun adapterUtil(): AdapterUtil
    fun layoutManagerUtil(): LayoutManagerUtil

    fun inject(context: BannerRecycler)
    fun inject(context: ComponentRecycler)
}