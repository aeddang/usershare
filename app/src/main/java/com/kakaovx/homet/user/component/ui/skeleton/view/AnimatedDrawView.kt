package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class AnimatedDrawView @kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val TAG = javaClass.simpleName

    private var disposable: Disposable? = null
    private var fps: Long = 1000/60
    private var frm:Int = 0
    protected var duration:Long = 0
    protected val currentTime:Long
        get() { return frm * fps}

    fun startAnimation( d:Long, delay:Long = 0 ) {
        frm = 0
        duration = d
        disposable = Observable.interval(fps, TimeUnit.MILLISECONDS)
            .timeInterval().delay(delay, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.computation())
            .subscribe {
                frm ++
                if(frm == 1) onStart()
                onCompute(frm)
                if (duration <= currentTime) {
                    stopAnimation()
                    onCompleted(frm)
                }
                postInvalidate()
            }
    }
    fun stopAnimation() {
        disposable?.dispose()
        disposable = null
    }

    private fun run(){
        frm++
        onCompute(frm)
        postInvalidate()
    }
    abstract fun onStart()
    abstract fun onCompute( f:Int )
    abstract fun onCompleted( f:Int )

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

}