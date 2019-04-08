package com.kakaovx.homet.user.component.repository

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.vxcore.VxPoseEstimator
import com.kakaovx.homet.user.util.AppExecutors
import com.kakaovx.homet.user.util.Log

class Repository(private val _executors: AppExecutors,
                 private val _restApi: RestfulApi,
                 val setting: SettingPreference,
                 val poseEstimator: VxPoseEstimator) {

    val TAG = javaClass.simpleName

    val executors: AppExecutors get() = _executors
    val restApi: RestfulApi get() = _restApi

    fun shunDownExecutors() {
        Log.i(TAG, "shunDownExecutors()")
//        executors.deviceIO.shutdown()
        if (!_executors.diskIO.isShutdown) _executors.diskIO.shutdown()
    }
}