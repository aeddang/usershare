package com.kakaovx.homet.user.component.ui.skeleton.view.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect


fun Bitmap.rotate(degree:Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val scaledBitmap = Bitmap.createScaledBitmap( this, width, height, true)
    return Bitmap.createBitmap( scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
}

fun Bitmap.crop(rectF:Rect): Bitmap {
    return Bitmap.createBitmap( this, rectF.left , rectF.top , rectF.width() , rectF.height() , null, false )
}

fun Bitmap.swapHolizental(): Bitmap {
    val matrix = Matrix()
    matrix.postScale(-1F,1F)
    val scaledBitmap = Bitmap.createScaledBitmap( this, width, height, true)
    return Bitmap.createBitmap( scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
}

fun Bitmap.swapVertical(): Bitmap {
    val matrix = Matrix()
    matrix.postScale(1F,-1F)
    val scaledBitmap = Bitmap.createScaledBitmap( this, width, height, true)
    return Bitmap.createBitmap( scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
}