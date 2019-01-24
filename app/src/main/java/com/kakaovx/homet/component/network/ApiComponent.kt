package com.kakaovx.homet.component.network

import com.kakaovx.homet.component.annotation.PageScope
import com.kakaovx.homet.component.app.AppComponent
import com.kakaovx.homet.component.network.api.GitHubApi
import com.kakaovx.homet.component.ui.skeleton.injecter.ApiPageFragment
import com.kakaovx.homet.component.ui.skeleton.injecter.ApiPageGestureFragment
import dagger.Component

@PageScope
@Component(dependencies = [AppComponent::class], modules = [ApiModule::class])
interface ApiComponent {
    fun inject(context: ApiPageGestureFragment)
    fun inject(context: ApiPageFragment)
}