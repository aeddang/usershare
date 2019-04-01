package com.kakaovx.homet.user.di.module.app

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.vxcore.VxCamera
import com.kakaovx.homet.user.component.vxcore.VxPoseEstimator
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CaptureModule {

    @Provides
    fun providePoseEstimator(app: Application, @Named("appContext") ctx: Context): VxPoseEstimator
            = VxPoseEstimator(app, ctx)

    @Provides
    fun provideCamera(@Named("appContext") ctx: Context): VxCamera
            = VxCamera(ctx)
}