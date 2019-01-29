package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import com.kakaovx.homet.user.App
import com.kakaovx.homet.user.component.api.Api
import com.kakaovx.homet.user.component.di.component.DaggerApiComponent
import com.kakaovx.homet.user.component.di.module.ApiModule
import com.kakaovx.homet.user.component.di.module.NetworkModule
import com.kakaovx.homet.user.component.di.module.PreferenceModule
import com.kakaovx.homet.user.component.di.module.ViewModelModule
import javax.inject.Inject

abstract class ApiPageDividedGestureFragment : InjectablePageDividedGestureFragment() {

    @Inject
    lateinit var api: Api

    @CallSuper
    override fun inject() {
        context?.run {
            DaggerApiComponent.builder()
                .appComponent(App.getAppComponent(this))
                .apiModule(ApiModule())
                .networkModule(NetworkModule())
                .preferenceModule(PreferenceModule())
                .viewModelModule(ViewModelModule())
                .build().inject(this@ApiPageDividedGestureFragment)
        }
    }

    @CallSuper
    override fun onCreated() {
        super.onCreated()
    }

}