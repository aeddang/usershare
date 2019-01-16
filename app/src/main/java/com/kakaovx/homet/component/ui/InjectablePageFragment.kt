package com.kakaovx.homet.component.page

import android.content.Context
import android.support.v4.app.Fragment
import android.support.annotation.CallSuper
import lib.page.PageFragment

abstract class InjectablePageFragment : PageFragment() {

    protected abstract fun inject(fragment: Fragment)

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        inject(this)
    }
}