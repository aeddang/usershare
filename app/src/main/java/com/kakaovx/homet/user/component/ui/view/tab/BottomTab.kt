package com.kakaovx.homet.user.component.ui.view.tab
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.component.ui.view.gif.FrameAnimation
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.ui_bottom_tab.view.*

class BottomTab: DivisionTab<PageID>{
    private val TAG = javaClass.simpleName

    companion object {
        private const val TOTAL_FRAME = 5
    }

    private lateinit var anis:ArrayList<FrameAnimation>
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onCreatedView() {
        super.onCreatedView()
        anis= ArrayList()
        anis.add(aniContent)
        anis.add(aniPlanner)
        anis.add(aniReport)
        anis.add(aniProfile)
        aniContent.initSet(R.drawable.cha_1_default,TOTAL_FRAME,1, true, 100)
        aniPlanner.initSet(R.drawable.cha_2_default,TOTAL_FRAME,1, true, 100)
        aniReport.initSet(R.drawable.cha_3_default,TOTAL_FRAME,1, true, 100)
        aniProfile.initSet(R.drawable.cha_4_default,TOTAL_FRAME,1, true, 100)

    }

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
            PageID.ACCOUNT
        )
    }

    fun setActivityPage(pageID: PageID){
        val groupID = Math.ceil((pageID.position/ 100).toDouble()).toInt()
        Log.d(TAG, "groupID $groupID")

        anis.forEachIndexed { index, frameAnimation ->
            if( groupID == index) frameAnimation.frame = (TOTAL_FRAME - 1)
            else frameAnimation.frame = 0
        }

    }


    private  var isView = true
    fun viewTab(){
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