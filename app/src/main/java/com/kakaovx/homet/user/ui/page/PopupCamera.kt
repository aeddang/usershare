package com.kakaovx.homet.user.ui.page

import android.transition.ChangeBounds
import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import kotlinx.android.synthetic.main.popup_capture_camera.*
import kotlinx.android.synthetic.main.ui_capture_camera.view.*


class PopupCamera : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName


    override fun getLayoutResId(): Int { return R.layout.popup_capture_camera }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    override fun onCreated() {
        super.onCreated()
        camera.onInit()
        sharedElementEnterTransition = ChangeBounds()
        camera.imageView.transitionName = "testAni"
    }


    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onPause() {
        super.onPause()
        camera.onPause()
    }

}