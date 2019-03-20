package com.kakaovx.homet.user.component.ui.skeleton.view.camera

import android.media.Image

import android.util.Size
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object CameraUtil{

    fun chooseOptimalSize( choices: Array<Size>, textureViewWidth: Int, textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size) : Size {
        val bigEnough = ArrayList<Size>()
        val notBigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                option.height == option.width * h / w
            ) {
                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> choices[0]
        }
    }

    internal class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            return java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
        }

    }

    internal class CompareRatioByArea(private val ratio:Float ): Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            val rL = Math.abs( lhs.width.toFloat() / lhs.height.toFloat() - ratio )
            val rR = Math.abs( rhs.width.toFloat() / rhs.height.toFloat() - ratio )
            return java.lang.Long.signum(rL.toLong() - rR.toLong())
        }

    }

    internal class ImageSaver internal constructor( private val mImage: Image, private val mFile: File) : Runnable {
        override fun run() {
            val buffer = mImage.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            var output: FileOutputStream? = null
            try {
                output = FileOutputStream(mFile)
                output.write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                mImage.close()
                if (null != output) {
                    try {
                        output.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }
}