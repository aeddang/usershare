package com.kakaovx.homet.user

import com.facebook.stetho.Stetho
import com.kakaovx.homet.user.component.di.component.DaggerAppComponent
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.Log
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class App: DaggerApplication() {

    private val TAG = javaClass.simpleName

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()

        if (AppFeature.APP_MEMORY_DEBUG) {
            Log.d(TAG, "Start Memory Debug")
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
        }

        if (AppFeature.APP_REMOTE_DEBUG) {
            Log.d(TAG, "Start Remote Debug")
            Stetho.initializeWithDefaults(this)
        }
    }
}