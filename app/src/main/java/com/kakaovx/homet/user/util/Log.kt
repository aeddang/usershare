package com.kakaovx.homet.user.util

import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature

object Log {

    fun i(tag: String, vararg objects: Any) {
        android.util.Log.i(AppConst.APP_TAG + tag, toString(*objects))
    }

    fun d(tag: String, vararg objects: Any) {
        if (AppFeature.APP_LOG_DEBUG) {
            android.util.Log.d(AppConst.APP_TAG + tag, toString(*objects))
        }
    }

    fun w(tag: String, vararg objects: Any) {
        android.util.Log.w(AppConst.APP_TAG + tag, toString(*objects))
    }

    fun e(tag: String, vararg objects: Any) {
        android.util.Log.e(AppConst.APP_TAG + tag, toString(*objects))
    }

    fun v(tag: String, vararg objects: Any) {
        android.util.Log.v(AppConst.APP_TAG + tag, toString(*objects))
    }

    private fun toString(vararg objects: Any): String {
        val sb = StringBuilder()
        for (o in objects) {
            sb.append(o)
        }
        return sb.toString()
    }
}