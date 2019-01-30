package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectableLinearLayout

abstract class DivisionTab<T> : InjectableLinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    var delegate: Delegate<T>? = null
    var selectedIdx:Int = -1; private set
    protected lateinit var tabs:Array<View>
    abstract fun getTabMenus(): Array<View>

    protected lateinit var datas:Array<T>
    abstract fun getIDDatas(): Array<T>

    var selectedTab:View? = null
    set(value) {
        selectedTab?.isSelected = false
        field = value
        value?.let {
            it.isSelected = true
            selectedIdx = tabs.indexOf(it)
        }
    }

    fun setSelect(id:T) {
        val idx = datas.indexOf(id)
        setSelect(idx)
    }

    fun setSelect(idx:Int) {
        if(idx < 0) return
        if(idx >= tabs.size) return
        selectedTab = tabs[idx]
    }

    override fun onCreated() {
        tabs = getTabMenus()
        tabs.forEach { it.setOnClickListener{view ->
            selectedTab = view
            delegate?.onSelected(this, selectedIdx)
            delegate?.onSelected(this, datas[selectedIdx])
        }}
    }

    interface Delegate <T> {
        fun onSelected(view:DivisionTab<T>, idx:Int){}
        fun onSelected(view:DivisionTab<T>, id:T){}
    }
}