package com.kakaovx.homet.user.di.module.view

import androidx.fragment.app.Fragment
import com.kakaovx.homet.user.ui.page.PopupPlayer
import dagger.Binds
import dagger.Module

@Module
abstract class PopupPlayerModule {
    @Binds
    internal abstract fun fragment(page: PopupPlayer): Fragment
}