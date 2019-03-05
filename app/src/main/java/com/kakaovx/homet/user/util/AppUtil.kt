package com.kakaovx.homet.user.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.kakaovx.homet.user.constant.AppConst
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

    fun getLiveDataListItemType(tag: String, index: Int) {
        when (index) {
            AppConst.HOMET_LIST_ITEM_INDEX -> {
                Log.d(tag, "HOMET_LIST_ITEM_INDEX")
            }
            AppConst.HOMET_LIST_ITEM_HOME_PROGRAM -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_PROGRAM")
            }
            AppConst.HOMET_LIST_ITEM_HOME_WORKOUT_TYPE -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_WORKOUT_TYPE")
            }
            AppConst.HOMET_LIST_ITEM_HOME_BANNER -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_BANNER")
            }
            AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_FREE_WORKOUT")
            }
            AppConst.HOMET_LIST_ITEM_HOME_TRAINER -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_TRAINER")
            }
            AppConst.HOMET_LIST_ITEM_HOME_ISSUE_TAG -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_ISSUE_TAG")
            }
            AppConst.HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM -> {
                Log.d(tag, "HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM")
            }
            AppConst.HOMET_LIST_ITEM_PROGRAM -> {
                Log.d(tag, "HOMET_LIST_ITEM_PROGRAM")
            }
            AppConst.HOMET_LIST_ITEM_WORKOUT -> {
                Log.d(tag, "HOMET_LIST_ITEM_WORKOUT")
            }
            AppConst.HOMET_LIST_ITEM_FREE_WORKOUT -> {
                Log.d(tag, "HOMET_LIST_ITEM_FREE_WORKOUT")
            }
            AppConst.HOMET_LIST_ITEM_TRAINER -> {
                Log.d(tag, "HOMET_LIST_ITEM_TRAINER")
            }
        }
    }
}