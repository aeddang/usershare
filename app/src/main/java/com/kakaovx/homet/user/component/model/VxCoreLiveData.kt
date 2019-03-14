package com.kakaovx.homet.user.component.model

import com.kakaovx.homet.user.constant.AppConst

class VxCoreLiveData {

    var cmd: Int = AppConst.LIVE_DATA_CMD_NONE
    var cameraCmd: Int = AppConst.HOMET_CAMERA_CMD_NONE
    var message: String? = null
    var data: IntArray? = null
}