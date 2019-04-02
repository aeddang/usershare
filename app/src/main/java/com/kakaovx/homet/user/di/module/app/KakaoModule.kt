package com.kakaovx.homet.user.di.module.app

import android.content.Context
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.vxcore.VxKakaoI
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class KakaoModule {

    @Provides
    fun provideKakaoI(@Named("appContext") ctx: Context, settingPreference: SettingPreference): VxKakaoI
            = VxKakaoI(ctx, settingPreference)

}