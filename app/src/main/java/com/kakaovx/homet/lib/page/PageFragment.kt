package com.kakaovx.homet.lib.page

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.kakaovx.homet.lib.constant.AnimationDuration
import com.kakaovx.homet.user.util.Log

abstract class PageFragment: Fragment(), Page {

    private val TAG = javaClass.simpleName
    var pageID:Any? = null; internal set


    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView()")
        return inflater.inflate(getLayoutResId(), container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreated()
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
        super.onDestroyView()
        onDestroyed()
        Log.d(TAG,"onDestroyView()")
    }

    open fun setParam(param:Map<String,Any>):PageFragment { return this }
    open fun isBackAble():Boolean { return true }

}