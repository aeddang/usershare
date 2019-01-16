package lib.page

import android.support.annotation.CallSuper
import android.view.View



abstract class PageGestureFragment:PageFragment(), PageGestureView.Delegate
{
    private lateinit var gestureView: PageGestureView
    private lateinit var contentsView:View
    private lateinit var backgroundView:View

    abstract fun getGestureView(): PageGestureView
    abstract fun getContentsView(): View
    abstract fun getBackgroundView(): View

    @CallSuper
    override fun init() {

        gestureView = getGestureView()
        contentsView = getContentsView()
        backgroundView = getBackgroundView()
    }

    override fun willCreateAnimation()
    {
        backgroundView.alpha = 0f
        gestureView.delegate = this
        gestureView.contentsView = contentsView

        val pageActivity = activity as PageActivity<*>
        pageActivity.let {
            val size = it.getPageAreaSize()
            gestureView.willCreateAnimation(size.second)
        }
    }

    override fun onCreateAnimation(): Long
    {
        gestureView.onGestureReturn(false)
        val duration = 300L
        backgroundView.animate().alpha(1f).setDuration(duration).withEndAction(animationCreateRunnable).start()
        return duration
    }

    override fun onDestroyAnimation(): Long
    {
        gestureView.onGestureClose(false)
        val duration = 300L
        backgroundView.animate().alpha(0f).setDuration(duration).withEndAction(animationDestroyRunnable).start()
        return duration
    }

    override fun onMove(view: PageGestureView, pct:Float)
    {
        backgroundView.alpha = pct
    }

    override fun onClose(view: PageGestureView)
    {
        pageID?.let {
            PagePresenter.getInstence<Any>()?.closePopup(it)
            return
        }
        onDestroyAnimation()
    }

    override fun onReturn(view: PageGestureView)
    {
        onCreateAnimation()
    }


}