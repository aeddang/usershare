package com.kakaovx.homet.user.component.ui.skeleton.rx

import androidx.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageFragment
import io.reactivex.disposables.CompositeDisposable

abstract class RxPageFragment : PageFragment() , Rx {

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreatedView() {
        onSubscribe()
    }

    @CallSuper
    override fun onDestroyedView() {
        disposables.clear()
    }
}