package com.kakaovx.homet.component.ui.module
import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableView
import kotlinx.android.synthetic.main.ui_holizontal_list.view.*

class BannerList: InjectableView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun getLayoutResId(): Int {
        return R.layout.ui_holizontal_list
    }

}