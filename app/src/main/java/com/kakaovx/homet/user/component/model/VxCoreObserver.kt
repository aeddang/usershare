package com.kakaovx.homet.user.component.model

import com.kakaovx.homet.user.constant.AppConst
import java.util.*

object VxCoreObserver: Observable() {

    var cmd = AppConst.HOMET_CAMERA_CMD_NONE

    private var liveData: VxCoreLiveData? = null

    fun setData(data: VxCoreLiveData) {
        liveData = data
        setChanged()
        notifyObservers()
    }

    fun getData() = liveData
}