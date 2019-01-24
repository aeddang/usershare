package com.kakaovx.homet.component.ui.module

import com.kakaovx.homet.component.annotation.ComponentScope
import com.kakaovx.homet.component.ui.BannerList
import dagger.Component

@ComponentScope
@Component( modules = [AdapterModule::class])
interface AdapterComponent {
    fun inject(context: BannerList)
}