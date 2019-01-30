package com.kakaovx.homet.user.component.ui.skeleton.rx

import android.support.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageDividedGestureFragment
import io.reactivex.disposables.CompositeDisposable

abstract class RxPageDividedGestureFragment : PageDividedGestureFragment(), rx {

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        super.onCreated()
        onSubscribe()
    }


    @CallSuper
    override fun onDestroied() {
        disposables.clear()
        super.onDestroy()
    }
}