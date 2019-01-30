package com.kakaovx.homet.user
import android.content.Context
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
import com.squareup.leakcanary.LeakCanary.refWatcher
import com.squareup.leakcanary.RefWatcher



class App: DaggerApplication() , HasSupportFragmentInjector {

    private val TAG = javaClass.simpleName

    private lateinit var refWatcher: RefWatcher
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }


    override fun onCreate() {
        super.onCreate()

        if (AppFeature.APP_MEMORY_DEBUG) {
            Log.d(TAG, "Start Memory Debug")
            if (LeakCanary.isInAnalyzerProcess(this)) return
            refWatcher = LeakCanary.install(this)
        }

        if (AppFeature.APP_REMOTE_DEBUG) {
            Log.d(TAG, "Start Remote Debug")
            Stetho.initializeWithDefaults(this)
        }
    }
    companion object {
        fun getRefWatcher(context: Context): RefWatcher {
            val application = context.getApplicationContext() as App
            return application.refWatcher
        }
    }


}