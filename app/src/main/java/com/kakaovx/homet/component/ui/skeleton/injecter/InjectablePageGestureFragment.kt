package com.kakaovx.homet.component.ui.skeleton.injecter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import lib.page.PageGestureFragment

abstract class InjectablePageGestureFragment : PageGestureFragment() , Injectable{

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