package com.kakaovx.homet.component.ui.skeleton.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableLinearLayout

abstract class DivisionTab : InjectableLinearLayout  {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    var delegate: Delegate? = null
    var selectedIdx:Int = -1; private set
    protected lateinit var tabs:Array<View>
    abstract fun getTabMenus(): Array<View>

    private  var selectedTab:View? = null
    set(value) {
        selectedTab?.isSelected = false
        field = value
        value?.let {
            it.isSelected = true
            selectedIdx = tabs.indexOf(it)
            delegate?.onSelected(this, selectedIdx)
        }
    }

    override fun onCreated() {
        tabs = getTabMenus()
        tabs.forEach { it.setOnClickListener{view -> selectedTab = view }}
    }

    interface Delegate {
        fun onSelected(view: DivisionTab, idx:Int)
    }
}