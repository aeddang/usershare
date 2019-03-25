package com.kakaovx.homet.user.component.ui.view.tab
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.ui.PageID
import kotlinx.android.synthetic.main.ui_bottom_tab.view.*

class BottomTab: DivisionTab<PageID>{

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onDestroyed() {}

    override fun getLayoutResId(): Int {
        return R.layout.ui_bottom_tab
    }

    override fun getTabMenu(): Array<View> {
        return arrayOf(
            tab_btn_home,
            tab_btn_content,
            tab_btn_planner,
            tab_btn_profile
        )
    }

    override fun getIDData(): Array<PageID> {
        return arrayOf (
            PageID.HOME,
            PageID.CONTENT,
            PageID.PROGRAM_PLAN,
            PageID.TEST
        )
    }
}