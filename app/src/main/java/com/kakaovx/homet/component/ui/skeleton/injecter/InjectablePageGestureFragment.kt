package com.kakaovx.homet.component.ui.skeleton.injecter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import lib.page.PageGestureFragment

abstract class InjectablePageGestureFragment : PageGestureFragment() {

    protected val disposables by lazy { CompositeDisposable() }
    protected open fun inject(){}

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        inject()
    }

    @CallSuper
    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}