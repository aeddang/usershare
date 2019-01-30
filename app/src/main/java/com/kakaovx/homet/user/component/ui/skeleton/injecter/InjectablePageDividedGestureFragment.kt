package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import com.kakaovx.homet.lib.page.PageDividedGestureFragment
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable

abstract class InjectablePageDividedGestureFragment : PageDividedGestureFragment(), Inject {

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        super.onCreated()
        onSubscribe()
        onInject()
    }


    @CallSuper
    override fun onDestroied() {
        disposables.clear()
        super.onDestroy()
    }
}