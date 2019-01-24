package com.kakaovx.homet.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import lib.page.PageDividedGestureFragment

abstract class InjectablePageDividedGestureFragment : PageDividedGestureFragment() , Injectable{

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        super.onCreated()
        inject()
        onSubscribe()
    }

    @CallSuper
    override fun onDestroied() {
        disposables.clear()
        super.onDestroy()
    }
}