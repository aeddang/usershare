package com.kakaovx.homet.user.util

import android.app.ActivityManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import io.reactivex.disposables.Disposable

operator fun Lifecycle.plusAssign(observer: LifecycleObserver)
        = this.addObserver(observer)

operator fun AppAutoClearedDisposable.plusAssign(disposable: Disposable)
        = this.add(disposable)

operator fun AppFragmentAutoClearedDisposable.plusAssign(disposable: Disposable)
        = this.add(disposable)

object AppUtil {

    fun getAppVersion(context: Context): String {
        var appVersion = "unknown"
        val appPackageName = context.packageName
        try {
            val pi = context.packageManager.getPackageInfo(appPackageName, 0)
            appVersion = pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appVersion
    }

    fun clearAppData(ctx: Context) {
        // clearing app data
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            // note: it has a return value!
            val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.clearApplicationUserData()
        } else {
            val packageName = ctx.packageName
            val runtime = Runtime.getRuntime()
            runtime.exec("pm clear $packageName")
        }
    }
}