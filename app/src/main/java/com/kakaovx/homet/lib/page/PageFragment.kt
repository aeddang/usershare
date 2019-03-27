package com.kakaovx.homet.lib.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.kakaovx.homet.user.util.Log

abstract class PageFragment: Fragment(), Page {


    private val TAG = javaClass.simpleName

    protected open var isRestoredPage:Boolean = false
    private var isInit:Boolean = true
    private var restoredView:View? = null
    var pageID:Any? = null; internal set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreatedView()")
        this.restoredView?.let { return it }
        return inflater.inflate(getLayoutResId(), container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if( isInit ) onCreatedView()
    }

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onAttached()
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        onDetached()
    }

    @CallSuper
    override fun onDestroyView() {
        if( isRestoredPage ){
            isInit = false
            this.restoredView = this.view
            super.onDestroyView()
            return
        } else {
            super.onDestroyView()
            onDestroyedView()
            Log.d(TAG,"onDestroyView()")
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        if( isRestoredPage ) {
            onDestroyedView()
            Log.d(TAG,"onDestroyView()")
        }
        Log.d(TAG,"onDestroy()")
    }

    open fun setParam(param:Map<String,Any>):PageFragment { return this }
    open fun isBackAble():Boolean { return true }

}