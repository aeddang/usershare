package com.kakaovx.homet.user.component.module.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.kakaovx.homet.user.R

class ImageFactory() {

    fun getBitmapLoader(context: Fragment): RequestBuilder<Bitmap> {
        return Glide.with(context).asBitmap()
    }

    fun getItemDrawableLoader (context:Fragment): RequestBuilder<Drawable> {
        return Glide.with(context).apply { RequestOptions().fitCenter().placeholder(R.drawable.ic_launcher_background).error( R.drawable.ic_launcher_background) }.asDrawable()
    }

    fun getBackgroundDrawableLoader(context: Fragment): RequestBuilder<Drawable> {
        return Glide.with(context).apply { RequestOptions().centerCrop().placeholder(R.drawable.ic_launcher_background).error( R.drawable.ic_launcher_background) }.asDrawable()
    }

}