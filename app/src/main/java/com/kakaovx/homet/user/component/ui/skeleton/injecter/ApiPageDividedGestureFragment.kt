package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import com.kakaovx.homet.user.App
import com.kakaovx.homet.user.component.network.module.ApiModule
import com.kakaovx.homet.user.component.network.DaggerApiComponent
import com.kakaovx.homet.user.component.network.module.ViewModelModule
import com.kakaovx.homet.user.component.network.viewmodel.ApiModelFactory

import javax.inject.Inject

abstract class ApiPageDividedGestureFragment : InjectablePageDividedGestureFragment() {
    @Inject
    lateinit var apiFactory : ApiModelFactory


    @CallSuper
    override fun inject() {
        context?.run {
            DaggerApiComponent.builder()
                .appComponent(App.getAppComponent(this))
                .apiModule(ApiModule())
                .viewModelModule(ViewModelModule())
                .build().inject(this@ApiPageDividedGestureFragment)
        }
    }

    @CallSuper
    override fun onCreated() {
        super.onCreated()
    }

}