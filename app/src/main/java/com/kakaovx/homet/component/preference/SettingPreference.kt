package com.kakaovx.homet.component.preference

import android.content.Context
import com.kakaovx.homet.constant.PreferenceName
import javax.inject.Inject

private const val PUSH_ENABLE = "push_enable"

class SettingPreference (context: Context) : CachedPreference(context, PreferenceName.SETTING) {

    fun setPushEnable(enable: Boolean) {
        put(PUSH_ENABLE, enable)
    }
    fun isPushEnable(): Boolean {
        return get(PUSH_ENABLE, false) as Boolean
    }
}