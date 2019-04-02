package com.kakaovx.homet.user.component.preference
import android.content.Context

class AccountPreference (context: Context) : CachedPreference(context, PreferenceName.LOGIN) {
    companion object {
        private const val LOGIN_TYPE = "login_type"
        private const val ACCESS_TOKEN = "acces_token"
        private const val PUSH_ENABLE = "push_enable"
    }

    fun setLoginData(type:Int, token:String) {
        put(LOGIN_TYPE, type)
        put(ACCESS_TOKEN, token)
    }
    fun getLoginData(): Pair<Int, String>{
        val type = get(LOGIN_TYPE, -1) as Int
        val token = get(ACCESS_TOKEN, "") as String
        return Pair(type, token)
    }

    fun setPushEnable(enable: Boolean) {
        put(PUSH_ENABLE, enable)
    }

    fun isPushEnable(): Boolean {
        return get(PUSH_ENABLE, false) as Boolean
    }
}