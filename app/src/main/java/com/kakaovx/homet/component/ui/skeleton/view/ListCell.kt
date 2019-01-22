package com.kakaovx.homet.component.ui.skeleton.view
import android.content.Context
import android.view.View
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableView

abstract class ListCell(context: Context) : InjectableView(context) {
    init {
        View.inflate(context, getLayoutResId(), this)
    }

    abstract fun <T> setData(data:T)
}