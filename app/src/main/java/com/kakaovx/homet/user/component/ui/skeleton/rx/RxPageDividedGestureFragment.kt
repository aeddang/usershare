package com.kakaovx.homet.user.component.ui.skeleton.rx

import androidx.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageDividedGestureFragment
import io.reactivex.disposables.CompositeDisposable

abstract class RxPageDividedGestureFragment : PageDividedGestureFragment(), Rx {

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        super.onCreated()
        onSubscribe()
    }

    @CallSuper
    override fun onDestroyed() {
        super.onDestroyed()
        disposables.clear()
    }
}