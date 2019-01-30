package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.support.annotation.CallSuper
import com.kakaovx.homet.lib.page.PageFragment
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable

abstract class InjectablePageFragment : PageFragment() , Inject{

    protected val disposables by lazy { CompositeDisposable() }

    @CallSuper
    override fun onCreated() {
        onSubscribe()
        onInject()
    }

    @CallSuper
    override fun onDestroied() {
        disposables.clear()
        super.onDestroy()
    }
}