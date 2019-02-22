package com.kakaovx.homet.user.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kakaovx.homet.user.constant.AppFeature
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class AppFragmentAutoClearedDisposable (
    private val lifecycleOwner: Fragment,
    private val alwaysClearOnStop: Boolean = true,
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
)
    : LifecycleObserver {

    private val TAG = "AppFragmentAutoClearedDisposable"

    fun add(disposable: Disposable) {
        if (AppFeature.APP_LOG_DEBUG) Log.i(TAG, "add(), ownerData = ${lifecycleOwner::class.java}")
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
        compositeDisposable.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "create(), ownerData = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "start(), ownerData = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "resume(), ownerData = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "pause(), ownerData = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "stop(), ownerData = ${lifecycleOwner::class.java}")
        if (!alwaysClearOnStop && !lifecycleOwner.isDetached) {
            return
        }
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "destroy(), ownerData = ${lifecycleOwner::class.java}")
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)
    }

}