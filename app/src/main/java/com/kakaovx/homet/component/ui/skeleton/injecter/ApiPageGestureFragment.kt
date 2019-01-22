package com.kakaovx.homet.component.ui.skeleton.injecter

import android.support.v4.app.Fragment
import com.kakaovx.homet.App
import com.kakaovx.homet.component.network.ApiModule
import com.kakaovx.homet.component.network.DaggerApiComponent
import com.kakaovx.homet.component.network.api.GitHubApi
import javax.inject.Inject

abstract class ApiPageGestureFragment : InjectablePageGestureFragment()
{
    @Inject
    lateinit var api: GitHubApi

    override fun inject() {
        context?.run {
            DaggerApiComponent.builder()
                .appComponent(App.getAppComponent(this)).apiModule(ApiModule())
                .build().inject(this@ApiPageGestureFragment)
        }
    }
}