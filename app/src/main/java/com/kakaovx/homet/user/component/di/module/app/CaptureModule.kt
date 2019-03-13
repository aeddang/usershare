package com.kakaovx.homet.user.component.di.module.app

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.vxcore.VxCamera
import com.kakaovx.homet.user.component.vxcore.VxMotionRecognition
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CaptureModule {

    @Provides
    fun provideCamera(@Named("appContext") ctx: Context): VxCamera
            = VxCamera(ctx)

    @Provides
    fun provideMotionRecognition(app: Application, @Named("appContext") ctx: Context): VxMotionRecognition
            = VxMotionRecognition(app, ctx)
}