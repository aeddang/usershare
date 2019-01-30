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

    override fun getLayoutResId(): Int { return R.layout.ui_bottom_tab }

    override fun getTabMenus(): Array<View>
    {
        return arrayOf(button0,button1,button2,button3,button4)
    }

    override fun getIDDatas(): Array<PageID> {
        return arrayOf (
            PageID.MAIN,
            PageID.SUB,
            PageID.TEST,
            PageID.SUB,
            PageID.SUB
        )
    }

}