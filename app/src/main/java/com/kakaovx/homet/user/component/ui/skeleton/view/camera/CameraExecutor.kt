package com.kakaovx.homet.user.component.ui.skeleton.view.camera

import android.os.Handler
import android.os.HandlerThread
import com.kakaovx.homet.user.util.Log
import java.util.concurrent.Executor

class CameraExecutor: Executor {

    val TAG = javaClass.simpleName

    private val backgroundThread = HandlerThread("DeviceBackground").also { it.start() }

    val backgroundHandler = Handler(backgroundThread.looper)

    override fun execute(command: Runnable) {
        backgroundHandler.post(command)
    }

    fun shutdown() {
        backgroundThread.quitSafely()
        try {
            backgroundThread.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }
}