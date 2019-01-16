package lib.page

import android.view.View


abstract class PageGestureFragment:PageFragment(), PageGestureView.Delegate
{
    protected var gestureView: PageGestureView? = null
    protected var contentsView:View? = null
    protected var backgroundView:View? =null

    override fun willCreateAnimation()
    {
        backgroundView?.alpha = 0f
        gestureView?.delegate = this
        gestureView?.contentsView = contentsView

        val pageActivity = activity as PageActivity<*>
        pageActivity?.let {
            val size = it.getPageAreaSize()
            gestureView?.willCreateAnimation(size.second.toFloat())
        }
    }

    override fun onCreateAnimation(): Long
    {
        gestureView?.onGestureReturn(false)
        val duration = 300L
        backgroundView?.let {it.animate().alpha(1f).setDuration(duration).withEndAction(animationCreateRunnable).start()}
        return duration
    }

    override fun onDestroyAnimation(): Long
    {
        gestureView?.onGestureClose(false)
        val duration = 300L
        backgroundView?.let {it.animate().alpha(0f).setDuration(duration).withEndAction(animationDestroyRunnable).start()}
        return duration
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        backgroundView = null
        gestureView = null
    }

    override fun onMove(view: PageGestureView, pct:Float)
    {
        backgroundView?.alpha = pct
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