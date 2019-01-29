package com.kakaovx.homet.user.component.api

import android.content.Context
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference

class Api(val context: Context, val restApi: RestfulApi, val setting: SettingPreference)