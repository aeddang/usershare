package com.kakaovx.homet.user.component.model

import com.kakaovx.homet.user.constant.AppConst
import java.util.*

class VxCoreLiveData {

    var cmd: Int = AppConst.LIVE_DATA_VX_CMD_NONE
    var cameraCmd: Int = AppConst.HOMET_CAMERA_CMD_NONE
    var kakaoiCmd: Int = AppConst.HOMET_KAKAOI_CMD_NONE
    var message: String? = null
    var state: Int = 0
    var data: IntArray? = null
    var poseData: ArrayList<Array<FloatArray>>? = null

    override fun toString(): String {
        return "VxCoreLiveData(cmd=$cmd, cameraCmd=$cameraCmd, kakaoiCmd=$kakaoiCmd)"
    }
}