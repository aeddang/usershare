package com.kakaovx.homet.user
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import kotlinx.android.synthetic.main.ui_bottom_tab.view.*

class BottomTab: DivisionTab {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun getLayoutResId(): Int {
        return R.layout.ui_bottom_tab
    }

    override fun getTabMenus(): Array<View>
    {
        return arrayOf(button0,button1,button2,button3,button4)
    }
}