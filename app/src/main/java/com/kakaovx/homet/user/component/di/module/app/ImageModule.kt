package com.kakaovx.homet.user.component.di.module.app
import com.kakaovx.homet.user.component.module.image.ImageFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class ImageModule {
    @Provides
    @Singleton
    fun provideImageFactory(): ImageFactory {
        return ImageFactory()
    }
}


