package com.kakaovx.homet.user.ui.page

import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.popup_test.*

class PopupTest : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.popup_test }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    override fun onCreated() {
        super.onCreated()
        Log.d(TAG, "onCreated()")
    }


}