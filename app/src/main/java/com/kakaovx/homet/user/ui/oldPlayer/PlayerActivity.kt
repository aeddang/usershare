package com.kakaovx.homet.user.ui.oldPlayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import dagger.android.support.DaggerAppCompatActivity


class PlayerActivity : DaggerAppCompatActivity() {

    val TAG = javaClass.simpleName

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val id = intent.getStringExtra(AppConst.HOMET_VALUE_MOTION_ID)
        val url = intent.getStringExtra(AppConst.HOMET_VALUE_VIDEO_URL)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(com.kakaovx.homet.user.R.id.container, PlayerFragment.newInstance(id, url))
                .commitNow()
        }
//        Log.d(TAG, "onCreate() default rotation = [${this.windowManager.defaultDisplay.rotation}]")
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent()")
        super.onNewIntent(intent)
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
    }

    @SuppressLint("NewApi")
    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }
}