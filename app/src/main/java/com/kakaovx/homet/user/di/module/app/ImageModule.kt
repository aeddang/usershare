package com.kakaovx.homet.user.di.module.app
import com.kakaovx.homet.user.component.image.ImageFactory
import dagger.Module
import dagger.Provides

@Module
class ImageModule {

    @Provides
    fun provideImageFactory(): ImageFactory {
        return ImageFactory()
    }
}