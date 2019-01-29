package com.kakaovx.homet.user.component.api

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.viewmodel.ApiModelFactory
import com.kakaovx.homet.user.component.preference.SettingPreference

class Api(val restApi: RestfulApi, val setting: SettingPreference, val apiFactory: ApiModelFactory)