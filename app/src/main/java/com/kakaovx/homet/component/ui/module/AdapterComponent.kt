package com.kakaovx.homet.component.ui.module

import com.kakaovx.homet.component.annotation.ComponentScope
import dagger.Component

@ComponentScope
@Component( modules = [DataModule::class, AdapterModule::class])
interface AdapterComponent {
    fun inject(context: BannerList)
}