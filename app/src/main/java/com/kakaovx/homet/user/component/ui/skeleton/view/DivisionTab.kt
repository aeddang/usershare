package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxLinearLayout
import com.kakaovx.homet.user.util.Log

abstract class DivisionTab<T> : RxLinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    private val TAG = javaClass.simpleName
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

    override fun onCreatedView() {
        data = getIDData()
        tab = getTabMenu()

    }

    override fun onSubscribe() {
        super.onSubscribe()
        tab.forEach {view ->
            view.clicks().subscribe {
                selectedTab = view
                delegate?.onSelected(this, selectedIdx)
                delegate?.onSelected(this, data[selectedIdx])
            }.apply { disposables?.add(this) }
        }
    }

    override fun onDestroyedView() {
        delegate = null
    }

    interface Delegate <T> {
        fun onSelected(view:DivisionTab<T>, idx:Int){}
        fun onSelected(view:DivisionTab<T>, id:T){}
    }
}