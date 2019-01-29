package com.kakaovx.homet.user.component.di.component

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.di.module.ApiModule
import com.kakaovx.homet.user.component.di.module.NetworkModule
import com.kakaovx.homet.user.component.di.module.PreferenceModule
import com.kakaovx.homet.user.component.di.module.ViewModelModule
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageDividedGestureFragment
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageFragment
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageGestureFragment
import com.kakaovx.homet.user.ui.splash.SplashFragment
import dagger.Component

@PageScope
@Component(
dependencies = [AppComponent::class],
modules = [
    ApiModule::class,
    NetworkModule::class,
    PreferenceModule::class,
    ViewModelModule::class
])

interface ApiComponent {
    fun context(): Context
    fun application(): Application
    fun inject(context: ApiPageDividedGestureFragment)
    fun inject(context: ApiPageGestureFragment)
    fun inject(context: ApiPageFragment)
    fun inject(context: SplashFragment)
}