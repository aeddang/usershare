package com.kakaovx.homet.user.component.repository

import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.preference.SettingPreference

class Repository(val restApi: RestfulApi, val setting: SettingPreference)