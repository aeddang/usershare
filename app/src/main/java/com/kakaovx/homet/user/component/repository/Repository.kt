package com.kakaovx.homet.user.component.repository

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.vxcore.VxCamera
import com.kakaovx.homet.user.component.vxcore.VxPoseEstimator
import com.kakaovx.homet.user.util.AppExecutors
import com.kakaovx.homet.user.util.Log

class Repository(val executors: AppExecutors,
                 val restApi: RestfulApi,
                 val setting: SettingPreference,
                 val camera: VxCamera,
                 val poseEstimator: VxPoseEstimator) {

    val TAG = javaClass.simpleName

    fun shunDownExecutors() {
        Log.i(TAG, "shunDownExecutors()")
//        executors.deviceIO.shutdown()
        if (!executors.diskIO.isShutdown) executors.diskIO.shutdown()
    }
}