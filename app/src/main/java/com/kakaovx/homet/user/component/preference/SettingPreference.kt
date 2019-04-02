package com.kakaovx.homet.user.component.preference

import android.content.Context
import com.kakaovx.homet.user.constant.AppConst

class SettingPreference (context: Context) : CachedPreference(context, PreferenceName.SETTING) {

    fun setPushEnable(enable: Boolean) {
        put(AppConst.PUSH_ENABLE, enable)
    }

    fun isPushEnable(): Boolean {
        return get(AppConst.PUSH_ENABLE, false) as Boolean
    }
}