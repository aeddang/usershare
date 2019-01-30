package com.kakaovx.homet.user
import android.support.v4.app.Fragment
import com.facebook.stetho.Stetho
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.Log
import com.squareup.leakcanary.LeakCanary
import com.kakaovx.homet.user.component.di.DaggerAppComponent
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.AndroidInjector




class App: DaggerApplication() , HasSupportFragmentInjector {


    private val TAG = javaClass.simpleName

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentInjector
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