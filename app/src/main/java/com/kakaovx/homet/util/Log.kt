package com.kakaovx.homet.util

object Log {

    fun i(tag: String, vararg objects: Any) {
        android.util.Log.i(AppUtil.APP_HOMET_TAG + tag, toString(*objects))
    }

    fun d(tag: String, vararg objects: Any) {
        if (AppUtil.APP_LOG_DEBUG) {
            android.util.Log.d(AppUtil.APP_HOMET_TAG + tag, toString(*objects))
        }
    }

    fun w(tag: String, vararg objects: Any) {
        android.util.Log.w(AppUtil.APP_HOMET_TAG + tag, toString(*objects))
    }

    fun e(tag: String, vararg objects: Any) {
        android.util.Log.e(AppUtil.APP_HOMET_TAG + tag, toString(*objects))
    }

    fun v(tag: String, vararg objects: Any) {
        android.util.Log.v(AppUtil.APP_HOMET_TAG + tag, toString(*objects))
    }

    private fun toString(vararg objects: Any): String {
        val sb = StringBuilder()
        for (o in objects) {
            sb.append(o)
        }
        return sb.toString()
    }
}