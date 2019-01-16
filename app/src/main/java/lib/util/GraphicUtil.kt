package lib.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.widget.ImageView


object GraphicUtil
{

    fun setSaturation(v: ImageView, saturation: Float)
    {
        val matrix = ColorMatrix()
        matrix.setSaturation(saturation)
        val cf = ColorMatrixColorFilter(matrix)
        v.colorFilter = cf
    }

    fun convertSaturation(d: Drawable, saturation: Float): Drawable
    {
        val matrix = ColorMatrix()
        matrix.setSaturation(saturation)
        val cf = ColorMatrixColorFilter(matrix)
        d.colorFilter = cf
        return d
    }

    fun getBitmapByByte(byteArray: ByteArray): Bitmap
    {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun getBitmapByDrawable(drawable: Drawable): Bitmap
    {
        val btd = drawable as BitmapDrawable
        return btd.bitmap
    }

    fun resizeImage(image: Bitmap, wid: Int, hei: Int): Bitmap
    {
        return Bitmap.createScaledBitmap(image, wid, hei, true)
    }

    fun resizeImageByWidth(image: Bitmap, wid: Int): Bitmap
    {
        val h = wid.toFloat() / image.width.toFloat() * image.height.toFloat()
        val hei = Math.floor(h.toDouble()).toInt()
        return Bitmap.createScaledBitmap(image, wid, hei, true)
    }

    fun resizeImageByHeight(image: Bitmap, hei: Int): Bitmap
    {
        val w = hei.toFloat() / image.height.toFloat() * image.width.toFloat()
        val wid = Math.floor(w.toDouble()).toInt()
        return Bitmap.createScaledBitmap(image, wid, hei, true)
    }

    fun resizeImageByScale(image: Bitmap, pct: Float): Bitmap
    {
        val wid = Math.floor((image.width.toFloat() * pct).toDouble()).toInt()
        val hei = Math.floor((image.height.toFloat() * pct).toDouble()).toInt()
        return Bitmap.createScaledBitmap(image, wid, hei, true)
    }

    fun flipImage(image: Bitmap, w: Int, h: Int): Bitmap
    {
        val mat = Matrix()
        mat.preScale(w.toFloat(), h.toFloat())
        return Bitmap.createBitmap(image, 0, 0, image.width, image.height, mat, true)
    }

    fun rotateImage(image: Bitmap, degree: Int): Bitmap
    {
        if (degree == 0)  return image
        var returnImg: Bitmap
        val mat = Matrix()
        mat.setRotate(degree.toFloat(), (image.width / 2).toFloat(), (image.height / 2).toFloat())
        try {
            returnImg = Bitmap.createBitmap(image, 0, 0, image.width, image.height, mat, true)
        } catch (e: OutOfMemoryError) {
            returnImg = image
        }
        return returnImg
    }

    fun cropBitmapImage(image: Bitmap, range: Rect): Bitmap?
    {
        try {
            return Bitmap.createBitmap(image, range.left, range.top, range.right, range.bottom)
        } catch (e: IllegalArgumentException) {
            return null
        }
    }

    fun cropImage(image: Drawable, range: Rect): Bitmap
    {
        val imageB = image as BitmapDrawable
        var bitMap = imageB.bitmap
        bitMap = Bitmap.createBitmap(bitMap, range.left, range.top, range.right, range.bottom)
        return bitMap
    }

    fun saveImageForAlbum(context: Context,image: Bitmap, imageName: String, imagePath: String)
    {
        val cr = context.contentResolver
        MediaStore.Images.Media.insertImage(cr, image, imageName, imagePath)

    }



}