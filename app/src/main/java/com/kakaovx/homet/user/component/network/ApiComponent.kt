package com.kakaovx.homet.user.component.network

import com.kakaovx.homet.user.component.annotation.PageScope
import com.kakaovx.homet.user.component.app.AppComponent
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageDividedGestureFragment
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageFragment
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageGestureFragment
import com.kakaovx.homet.user.ui.splash.SplashFragment
import com.kakaovx.homet.user.component.network.module.ApiModule
import dagger.Component

@PageScope
@Component(dependencies = [AppComponent::class], modules = [ApiModule::class])
interface ApiComponent {
    fun inject(context: ApiPageDividedGestureFragment)
    fun inject(context: ApiPageGestureFragment)
    fun inject(context: ApiPageFragment)
    fun inject(context: SplashFragment)
}