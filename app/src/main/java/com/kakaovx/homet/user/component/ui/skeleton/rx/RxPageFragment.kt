package com.kakaovx.homet.user.component.ui.skeleton.rx

import android.support.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageFragment
import io.reactivex.disposables.CompositeDisposable

abstract class RxPageFragment : PageFragment() , Rx {

//    private val appTag = javaClass.simpleName

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
//        Log.d(appTag, "onCreated()")
        onSubscribe()
    }

    @CallSuper
    override fun onDestroyed() {
//        Log.d(appTag, "onDestroyed()")
        disposables.clear()
        super.onDestroy()
    }
}