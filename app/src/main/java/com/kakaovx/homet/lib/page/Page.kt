package com.kakaovx.homet.lib.page

import android.support.annotation.LayoutRes

interface Page {
    @LayoutRes
    fun getLayoutResId(): Int
    fun <D> setData(data:D){}
    fun onCreated(){}
    fun onAttached(){}
    fun onDetached(){}
    fun onDestroied(){}
}