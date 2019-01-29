package com.kakaovx.homet.user

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.kakaovx.homet.user.component.di.component.AppComponent
import com.kakaovx.homet.user.component.di.component.DaggerAppComponent
import com.kakaovx.homet.user.component.di.module.AppModule
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.Log
import com.squareup.leakcanary.LeakCanary


class App: Application() {

    private val TAG = javaClass.simpleName

    val singleton: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as App).singleton
        }
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