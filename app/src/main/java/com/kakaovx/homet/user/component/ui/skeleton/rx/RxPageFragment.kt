package com.kakaovx.homet.user.component.ui.skeleton.rx

import android.support.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageFragment
import io.reactivex.disposables.CompositeDisposable

abstract class RxPageFragment : PageFragment() , rx{

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        onSubscribe()
    }

    @CallSuper
    override fun onDestroied() {
        disposables.clear()
        super.onDestroy()
    }
}