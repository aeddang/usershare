package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import com.kakaovx.homet.user.component.api.Api
import javax.inject.Inject

abstract class ApiPageFragment : InjectablePageFragment() {

    @Inject
    lateinit var api: Api

    @CallSuper
    override fun inject() {
//        context?.run {
//            DaggerApiComponent.builder()
//                .appComponent(App.getAppComponent(this))
//                .apiModule(ApiModule())
//                .networkModule(NetworkModule())
//                .preferenceModule(PreferenceModule())
//                .viewModelModule(ViewModelModule())
//                .build()
//                .inject(this@ApiPageFragment)
//        }
    }
}