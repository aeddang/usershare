package lib.page

import android.os.Bundle
import android.os.Handler
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

abstract class PageFragment:Fragment()
{
    enum class PageType
    {
        DEFAULT,BACK,POPUP
    }
    private var animationDuration = 200L
    private var animationHandler: Handler = Handler()
    private var viewCreateRunnable: Runnable = Runnable {onCreateAnimation()}
    protected var animationCreateRunnable: Runnable = Runnable {didCreateAnimation()}
    protected var animationDestroyRunnable: Runnable = Runnable {didDestroyAnimation()}

    var pageID:Any? = null
        internal set

    internal var delegate:Delegate? = null
    internal var pageType = PageType.DEFAULT

    @LayoutRes protected abstract fun getLayoutResId(): Int
    abstract fun init()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        willCreateAnimation()
        animationHandler.post(viewCreateRunnable)
    }

    open fun willCreateAnimation()
    {
        val pageActivity = activity as PageActivity<*>
        val size = pageActivity.getPageAreaSize()
        var posX = 0f
        var posY = 0f
        when (pageType) {
            PageType.DEFAULT -> posX = -size.first
            PageType.BACK -> posX = size.first
            PageType.POPUP -> posY = size.second
        }
        view?.translationX = posX
        view?.translationY = posY
    }

    open fun onCreateAnimation():Long
    {
        var interpolator:Interpolator? = null
        when (pageType) {
            PageType.DEFAULT -> interpolator = LinearInterpolator()
            PageType.BACK -> interpolator = LinearInterpolator()
            PageType.POPUP -> interpolator = DecelerateInterpolator()

        }
        view?.let {
            it.animate()
            .translationX(0f)
            .translationY(0f)
            .setInterpolator(interpolator)
            .setDuration(animationDuration)
            .withEndAction(animationCreateRunnable)
            .start()
        }
        delegate?.onCreateAnimation(this)
        return animationDuration
    }
    protected open fun didCreateAnimation(){}
    open fun onDestroyAnimation():Long
    {
         view?.let {
            var posX = 0f
            var posY = 0f
            var interpolator:Interpolator? = null
            when (pageType) {
                PageType.DEFAULT ->
                {
                    posX = it.width.toFloat()
                    interpolator = LinearInterpolator()
                }
                PageType.BACK ->
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
            it.animate()
                .translationX(posX)
                .translationY(posY)
                .setInterpolator(interpolator)
                .setDuration(animationDuration)
                .withEndAction(animationDestroyRunnable)
                .start()
        }
        return animationDuration
    }

    protected open fun didDestroyAnimation()
    {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    @CallSuper
    override fun onDestroyView()
    {
        super.onDestroyView()
        view?.let{
            val parent = it.parent as ViewGroup?
            parent?.removeView(it)
        }
        delegate = null
        animationHandler.removeCallbacks(viewCreateRunnable)
        Log.d(PagePresenter.TAG,"onDestroyView")
    }

    open fun onBack():Boolean {return true}

    internal interface Delegate
    {
        fun onCreateAnimation(v:PageFragment)
    }
}