package lib.page

import android.support.annotation.LayoutRes

interface Page {
    @LayoutRes
    fun getLayoutResId(): Int
    fun onCreated(){}
    fun onAttached(){}
    fun onDetached(){}
    fun onDestroied(){}
}