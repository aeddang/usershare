package com.kakaovx.homet.user.ui.page

import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectablePageDividedGestureFragment
import com.kakaovx.homet.lib.page.PageGestureView
import kotlinx.android.synthetic.main.popup_test.*

class PopupTest : InjectablePageDividedGestureFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.popup_test }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    override fun inject() {

    }

    override fun onCreated() {
        super.onCreated()
    }

}