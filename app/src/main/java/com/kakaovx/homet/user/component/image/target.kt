package com.kakaovx.homet.user.component.module.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

typealias BitmapCompleteHandler = (Bitmap) -> Unit
typealias DrawableCompleteHandler = (Drawable) -> Unit

open class SimpleDrawableTarget(width:Int, height:Int, val completeHandler:DrawableCompleteHandler): CustomTarget<Drawable>(width, height){
    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        completeHandler(resource)
    }
    override fun onLoadCleared(placeholder: Drawable?) {}
}

open class SimpleBitmapTarget(width:Int, height:Int, val completeHandler:BitmapCompleteHandler): CustomTarget<Bitmap>(width, height){
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        completeHandler(resource)
    }
    override fun onLoadCleared(placeholder: Drawable?) {}
}