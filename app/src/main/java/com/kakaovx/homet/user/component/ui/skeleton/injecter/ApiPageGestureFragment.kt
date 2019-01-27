package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import com.kakaovx.homet.user.App
import com.kakaovx.homet.user.component.network.module.ApiModule
import com.kakaovx.homet.user.component.network.DaggerApiComponent
import com.kakaovx.homet.user.component.network.api.GitHubApi
import javax.inject.Inject

abstract class ApiPageGestureFragment : InjectablePageGestureFragment() {
    @Inject
    lateinit var api: GitHubApi

    @CallSuper
    override fun inject() {
        context?.run {
            DaggerApiComponent.builder()
                .appComponent(App.getAppComponent(this)).apiModule(ApiModule())
                .build().inject(this@ApiPageGestureFragment)
        }
    }
}