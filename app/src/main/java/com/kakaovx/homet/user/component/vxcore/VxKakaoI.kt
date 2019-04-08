package com.kakaovx.homet.user.component.vxcore

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.preference.SettingPreference

class VxKakaoI(val context: Context, val settings: SettingPreference) {

    private val TAG = javaClass.simpleName

    private var _core: MutableLiveData<VxCoreLiveData>? = null
    val core: LiveData<VxCoreLiveData>? get() = _core
}