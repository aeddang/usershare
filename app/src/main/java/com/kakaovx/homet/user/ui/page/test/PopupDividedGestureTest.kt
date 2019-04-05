package com.kakaovx.homet.user.ui.page.test

import android.graphics.drawable.Drawable
import android.transition.ChangeBounds
import android.view.View
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import kotlinx.android.synthetic.main.popup_test.*

class PopupDividedGestureTest : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName

    companion object {
        const val SHARE_IMAGE_KEY = "shareImage"
        const val SHARE_IMAGE_NAME = "testImage"
    }

    /**
     * layout bind
     * getLayoutResId
     * getGestureView 터치가능 영역
     * getContentsView 터이이동시 움직이는 영역
     * getBackgroundView ContentsView 아래 배경 터치이동시 alpha값 변화
     * getDividedView 세로스트롤등으로 터치 다운 이벤트가 겹치는영역분리  ContentsView와 함께 움직임
     */
    override fun getLayoutResId(): Int { return R.layout.popup_test } //공통
    override fun getGestureView(): PageGestureView { return gestureView } //RxPageGestureFragment, RxPageDividedGestureFragment
    override fun getContentsView(): View { return contents } //RxPageGestureFragment, RxPageDividedGestureFragment
    override fun getBackgroundView(): View { return bg } //RxPageGestureFragment, RxPageDividedGestureFragment
    override fun getDividedView(): View { return divided }  //only RxPageDividedGestureFragment


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