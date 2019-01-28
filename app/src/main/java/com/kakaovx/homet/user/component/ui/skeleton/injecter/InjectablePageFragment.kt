package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageFragment
import io.reactivex.disposables.CompositeDisposable

abstract class InjectablePageFragment : PageFragment() , Injectable {

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        inject()
        onSubscribe()
    }

    @CallSuper
    override fun onDestroied() {
        disposables.clear()
        super.onDestroy()
    }
}