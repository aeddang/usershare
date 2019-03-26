package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxLinearLayout

abstract class DivisionTab<T> : RxLinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    var delegate: Delegate<T>? = null

    private var selectedIdx:Int = -1

    lateinit var tab:Array<View>
    abstract fun getTabMenu(): Array<View>

    protected lateinit var data:Array<T>
    abstract fun getIDData(): Array<T>

    var selectedTab:View? = null
    set(value) {
        selectedTab?.isSelected = false
        field = value
        value?.let {
            it.isSelected = true
            selectedIdx = tab.indexOf(it)
        }
    }

    fun setSelect(id:T) {
        val idx = data.indexOf(id)
        setSelect(idx)
    }

    fun setSelect(idx:Int) {
        if(idx < 0) return
        if(idx >= tab.size) return
        selectedTab = tab[idx]
    }

    override fun onCreated() {
        data = getIDData()
        tab = getTabMenu()
        tab.forEach { it.setOnClickListener{view ->
            selectedTab = view
            delegate?.onSelected(this, selectedIdx)
            delegate?.onSelected(this, data[selectedIdx])
        }}
    }

    override fun onDestroyed() {
        delegate = null
    }

    interface Delegate <T> {
        fun onSelected(view:DivisionTab<T>, idx:Int){}
        fun onSelected(view:DivisionTab<T>, id:T){}
    }
}