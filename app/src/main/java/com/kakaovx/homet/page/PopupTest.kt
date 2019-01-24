package com.kakaovx.homet.page
import android.view.View
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectablePageDividedGestureFragment
import kotlinx.android.synthetic.main.popup_test.*
import lib.page.PageGestureView



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