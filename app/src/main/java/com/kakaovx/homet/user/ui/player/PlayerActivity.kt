package com.kakaovx.homet.user.ui.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class PlayerActivity : DaggerAppCompatActivity() {

    val TAG = javaClass.simpleName

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val id = intent.getStringExtra(AppConst.HOMET_VALUE_MOTION_ID)
        val url = intent.getStringExtra(AppConst.HOMET_VALUE_VIDEO_URL)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment.newInstance(id, url))
                .commitNow()
        }
        Log.d(TAG, "onCreate() default rotation = [${this.windowManager.defaultDisplay.rotation}]")
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
    }
}