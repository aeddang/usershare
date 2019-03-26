package com.kakaovx.homet.user.component.ui.skeleton.thread

import android.os.Handler
import android.os.HandlerThread
import com.kakaovx.homet.user.util.Log

class HandlerExecutor(val name:String): ThreadExecutor {

    val TAG = javaClass.simpleName

    private val backgroundThread = HandlerThread(name).also { it.start() }
    val backgroundHandler = Handler(backgroundThread.looper)

    override fun execute(command: Runnable) {
        backgroundHandler.post(command)
    }

    override fun shutdown( isSafe:Boolean ) {
        if( isSafe )  backgroundThread.quitSafely() else backgroundThread.quit()
        try {
            backgroundThread.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }
}