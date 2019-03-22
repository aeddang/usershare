package com.kakaovx.homet.user.ui.page

import android.transition.ChangeBounds
import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import com.kakaovx.homet.user.component.ui.view.camera.capture
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.popup_capture_camera.*



class PopupCamera : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName
    override fun getLayoutResId(): Int { return R.layout.popup_capture_camera }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    override fun onCreated() {
        super.onCreated()
        sharedElementEnterTransition = ChangeBounds()
        capturedView.transitionName = "testAni"
    }

    override fun onSubscribe() {
        super.onSubscribe()
        camera.capture().subscribe { bitmap-> capturedView.setImageBitmap(bitmap) }.apply { disposables.add(this) }
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