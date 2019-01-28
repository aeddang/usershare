package com.kakaovx.homet.user.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class AppAutoClearedDisposable (
    private val lifecycleOwner: AppCompatActivity,
    private val alwaysClearOnStop: Boolean = true,
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
)
    : LifecycleObserver {

    private val TAG = "AutoClearedDisposable"

    fun add(disposable: Disposable) {
        if (AppFeature.APP_LOG_DEBUG) Log.i(TAG, "add(), owner = ${lifecycleOwner::class.java}")
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
        compositeDisposable.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "create(), owner = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "start(), owner = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "resume(), owner = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "pause(), owner = ${lifecycleOwner::class.java}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "stop(), owner = ${lifecycleOwner::class.java}")
        if (!alwaysClearOnStop && !lifecycleOwner.isFinishing) {
            return
        }
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        if (AppFeature.APP_LOG_DEBUG) Log.d(TAG, "destroy(), owner = ${lifecycleOwner::class.java}")
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)
    }

}