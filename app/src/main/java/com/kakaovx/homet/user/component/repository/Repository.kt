package com.kakaovx.homet.user.component.repository

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.util.AppExecutors
import com.kakaovx.homet.user.util.Log

class Repository(val executors: AppExecutors,
                 val restApi: RestfulApi,
                 val setting: SettingPreference) {

    val TAG = javaClass.simpleName

    fun shunDownExecutors() {
        Log.i(TAG, "shunDownExecutors()")
//        executors.deviceIO.shutdown()
        if (!executors.diskIO.isShutdown) executors.diskIO.shutdown()
    }
}