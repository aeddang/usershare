package com.kakaovx.homet

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.kakaovx.homet.component.app.AppComponent
import com.kakaovx.homet.component.app.AppModule
import com.kakaovx.homet.component.app.DaggerAppComponent
import com.kakaovx.homet.component.network.NetworkModule
import com.kakaovx.homet.component.preference.PreferenceModule
import com.kakaovx.homet.util.AppUtil
import com.kakaovx.homet.util.Log
import com.squareup.leakcanary.LeakCanary


class App: Application() {

    private val TAG = javaClass.simpleName

    val singleton: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .preferenceModule(PreferenceModule(this))
            .build()
    }

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as App).singleton
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (AppUtil.APP_MEMORY_DEBUG) {
            Log.d(TAG, "Start Memory Debug")
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
        }

        if (AppUtil.APP_REMOTE_DEBUG) {
            Log.d(TAG, "Start Remote Debug")
            Stetho.initializeWithDefaults(this)
        }
    }
}