package com.kakaovx.homet.user.di.module.app

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.component.vxcore.VxPoseEstimator
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CaptureModule {

    @Provides
    fun providePoseEstimator(app: Application, @Named("appContext") ctx: Context): VxPoseEstimator
            = VxPoseEstimator(app, ctx)
}