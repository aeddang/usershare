package com.kakaovx.homet.page
import android.support.v4.app.Fragment
import android.view.View
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectablePageGestureFragment
import kotlinx.android.synthetic.main.popup_test.*
import lib.page.PageGestureView


class PopupTest : InjectablePageGestureFragment()
{
    override fun getLayoutResId(): Int { return R.layout.popup_test }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }

    override fun inject() {

    }

    override fun onCreated() {
        super.onCreated()
    }

}