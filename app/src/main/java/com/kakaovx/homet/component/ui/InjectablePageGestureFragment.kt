package com.kakaovx.homet.component.ui

import android.content.Context
import android.support.v4.app.Fragment
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import lib.page.PageFragment
import lib.page.PageGestureFragment

abstract class InjectablePageGestureFragment : PageGestureFragment() {

    protected val disposables by lazy { CompositeDisposable() }
    protected abstract fun inject(fragment: Fragment)

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        inject(this)
    }

    @CallSuper
    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}