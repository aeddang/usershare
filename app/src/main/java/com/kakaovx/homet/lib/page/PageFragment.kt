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

    enum class PageType {
        INIT, IN, OUT, POPUP
    }

    protected var animationDuration = AnimationDuration.SHORT.duration
    protected var animationHandler: Handler = Handler()
    protected var viewCreateRunnable: Runnable = Runnable {onCreateAnimation()}
    protected var animationCreateRunnable: Runnable = Runnable {didCreateAnimation()}
    protected var animationDestroyRunnable: Runnable = Runnable {didDestroyAnimation()}
    var pageID:Any? = null; internal set
    internal var delegate:Delegate? = null
    internal var pageType = PageType.INIT
    private var animation: ViewPropertyAnimator? = null

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView()")
        return inflater.inflate(getLayoutResId(), container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreated()
        willCreateAnimation()
        animationHandler.post(viewCreateRunnable)
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

    protected open fun willCreateAnimation() {
        val pageActivity = activity as PageActivity<*>
        val size = pageActivity.getPageAreaSize()
        var posX = 0f
        var posY = 0f
        var valueAlpha = 1f
        when (pageType) {
            PageType.INIT -> valueAlpha = 0f
            PageType.IN -> posX = -size.first
            PageType.OUT -> posX = size.first
            PageType.POPUP -> posY = size.second
        }
        view?.translationX = posX
        view?.translationY = posY
        view?.alpha = valueAlpha
    }

    protected open fun onCreateAnimation():Long {
        var interpolator:Interpolator? = null
        when (pageType) {
            PageType.INIT -> DecelerateInterpolator()
            PageType.IN -> LinearInterpolator()
            PageType.OUT -> LinearInterpolator()
            PageType.POPUP -> DecelerateInterpolator()
        }
        animation?.cancel()
        view?.let {
            animation = it.animate()
            animation!!.translationX(0f)
            .translationY(0f).alpha(1f)
            .setInterpolator(interpolator)
            .setDuration(animationDuration)
            .withEndAction(animationCreateRunnable)
            .start()
        }
        delegate?.onCreateAnimation(this)
        return animationDuration
    }
    protected open fun didCreateAnimation() {}

    internal open fun onClosePopupAnimation():Long {
        return onDestroyAnimation()
    }
    internal open fun onDestroyAnimation():Long {
        animation?.cancel()
        view?.let {
            var posX = 0f
            var posY = 0f
            var interpolator:Interpolator? = null
            when (pageType) {
                PageType.IN, PageType.INIT ->
                {
                    posX = it.width.toFloat()
                    interpolator = LinearInterpolator()
                }
                PageType.OUT ->
                {
                    posX = -it.width.toFloat()
                    interpolator = LinearInterpolator()
                }
                PageType.POPUP ->
                {
                    posY = it.height.toFloat()
                    interpolator = DecelerateInterpolator()
                }
            }
            animation = it.animate()
            animation!!.translationX(posX)
                .translationY(posY)
                .setInterpolator(interpolator)
                .setDuration(animationDuration)
                .withEndAction(animationDestroyRunnable)
                .start()
        }
        return animationDuration
    }

    protected open fun didDestroyAnimation() {
        Log.d(TAG,"didDestroyAnimation()")
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commitNow()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        animationHandler.removeCallbacks(viewCreateRunnable)
        view?.let{
            val parent = it.parent as ViewGroup?
            parent?.removeView(it)
        }
        animation?.cancel()
        animation = null
        delegate = null
        onDestroyed()
        Log.d(TAG,"onDestroyView()")
    }

    open fun setParam(param:Map<String,Any>):PageFragment { return this }
    open fun isBackAble():Boolean { return true }

    internal interface Delegate {
        fun onCreateAnimation(v:PageFragment)
    }
}