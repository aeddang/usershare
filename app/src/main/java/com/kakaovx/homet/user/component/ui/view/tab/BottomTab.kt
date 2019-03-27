package com.kakaovx.homet.user.component.ui.view.tab
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.ui_bottom_tab.view.*

class BottomTab: DivisionTab<PageID>{
    private val TAG = javaClass.simpleName
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    override fun onDestroyedView() {}
    override fun getLayoutResId(): Int { return R.layout.ui_bottom_tab }

    override fun getTabMenu(): Array<View> {
        return arrayOf(
            tab_btn_content,
            tab_btn_planner,
            tab_btn_report,
            tab_btn_profile
        )
    }

    override fun getIDData(): Array<PageID> {
        return arrayOf (
            PageID.CONTENT,
            PageID.PROGRAM_PLAN,
            PageID.PROGRAM_REPORT,
            PageID.TEST
        )
    }


    private  var isView = true
    fun viewTab(){

        Log.d(TAG, "viewTab = {$isView}")
        if(isView) return
        isView = true
        val layout = layoutParams as FrameLayout.LayoutParams
        layout.bottomMargin = 0
        layoutParams = layout
        val moveAni = TranslateAnimation(0.0f, 0.0f, height.toFloat() , 0f)
        moveAni.interpolator = AccelerateInterpolator()
        moveAni.duration = 300
        startAnimation(moveAni)
    }

    fun hideTab(){
        Log.d(TAG, "hideTab = {$isView}")
        if(!isView) return
        isView = false

        val layout = layoutParams as FrameLayout.LayoutParams
        layout.bottomMargin = -height
        layoutParams = layout
        val moveAni = TranslateAnimation(0.0f, 0.0f, -height.toFloat() , 0f)
        moveAni.interpolator = DecelerateInterpolator()
        moveAni.duration = 300
        startAnimation(moveAni)
    }
}