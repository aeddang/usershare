package com.kakaovx.homet.user.component.preference
import android.content.Context

private const val LOGIN_TYPE = "login_type"
private const val ACCESS_TOKEN = "acces_token"

class MemberPreference (context: Context) : CachedPreference(context, PreferenceName.LOGIN) {

    fun setLoginData(type:Int, token:String) {
        put(LOGIN_TYPE, type)
        put(ACCESS_TOKEN, token)
    }
    fun getLoginData(): Pair<Int, String>{
        val type = get(LOGIN_TYPE, -1) as Int
        val token = get(ACCESS_TOKEN, "") as String
        return Pair(type, token)
    }
}