package com.kakaovx.homet.user.ui.page.test

import android.graphics.drawable.Drawable
import android.transition.ChangeBounds
import android.view.View
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.popup_test.*

class PopupDividedGestureTest : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName

    companion object {
        const val SHARE_IMAGE_KEY = "shareImage"
        const val SHARE_IMAGE_NAME = "testImage"
    }

    override fun getLayoutResId(): Int { return R.layout.popup_test }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }


    private  var image:Drawable? = null


    override fun setParam(param: Map<String, Any>): PageFragment {
        image = param[SHARE_IMAGE_KEY] as Drawable
        /**
          view 구성전 setParam이 발생하기 때문에 메모리후 onCreatedView 에서 바인딩
         */
        return super.setParam(param)
    }

    override fun onCreatedView() {
        super.onCreatedView()

        imageView.setImageDrawable(image)
        sharedElementEnterTransition = ChangeBounds()
        imageView.transitionName = SHARE_IMAGE_NAME

    }


}