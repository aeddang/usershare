package lib.page

import android.support.annotation.CallSuper
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import lib.constant.AnimationDuration
import lib.ui.Gesture

abstract class PageNavigationActivity<T> :  PageActivity<T>(), PageGestureView.Delegate {
    private lateinit var navigationView: PageGestureView
    private lateinit var contentsView: View
    private lateinit var navigationViewBgView: View

    abstract fun getNavigationView(): PageGestureView
    abstract fun getContentsView(): View
    abstract fun getNavigationViewBgView(): View
    abstract fun getCloseType(): Gesture.Type

    private lateinit var closeType:Gesture.Type

    private var animation: ViewPropertyAnimator? = null
    private var animationHideRunnable: Runnable = Runnable {didHideAnimation()}
    private var animationShowRunnable: Runnable = Runnable {didShowAnimation()}
    private var isShow = false

    @CallSuper
    override fun init() {
        closeType = getCloseType()
        navigationView = getNavigationView()
        contentsView = getContentsView()
        navigationViewBgView = getNavigationViewBgView()
        navigationViewBgView.alpha = 0f
        navigationView.delegate = this
        navigationView.contentsView = contentsView
        navigationView.closeType = closeType

        this.didHideAnimation()
        this.navigationView.setGestureClose()
    }

    fun onToggleNavigation() {
        if( isShow ) onHideNavigation() else onShowNavigation()
    }

    override fun onShowNavigation() {
        isShow = true
        navigationViewBgView.visibility = View.VISIBLE
        navigationView.visibility = View.VISIBLE
        animation?.cancel()
        navigationView.onGestureReturn()
    }

    override fun onHideNavigation() {
        animation?.cancel()
        isShow = false
        navigationView.onGestureClose()
    }

    override fun onAnimate(view: PageGestureView, pos: Float) {
        movePageArea(pos - navigationView.contentSize)
    }

    override fun onMove(view: PageGestureView, pct:Float) {
        navigationViewBgView.alpha = pct
        movePageArea(navigationView.contentSize * pct)
    }

    protected open fun movePageArea(pos:Float) {
        if (navigationView.isHorizontal) pageArea.translationX = pos else pageArea.translationY = pos
    }

    override fun onClose(view: PageGestureView) {
        isShow = false
        animation?.cancel()
        animation = navigationViewBgView.animate()
        animation?.alpha(0f)?.setDuration(AnimationDuration.SHORT.duration)?.withEndAction(animationHideRunnable)?.start()
    }

    override fun onReturn(view: PageGestureView) {
        animation?.cancel()
        animation = navigationViewBgView.animate()
        animation?.alpha(1f)?.setDuration(AnimationDuration.SHORT.duration)?.withEndAction(animationShowRunnable)?.start()
    }

    protected open fun didShowAnimation() {}
    protected open fun didHideAnimation() {
        navigationViewBgView.visibility = View.GONE
        navigationView.visibility = View.GONE
    }




}