package lib

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.provider.MediaStore
import android.widget.FrameLayout
import android.widget.ImageView
import lib.util.CommonUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

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

    fun getFileByBitmap(context: Context, btm: Bitmap, filename: String): File?
    {
        val f = File(context.cacheDir, filename)
        try {
            f.createNewFile()
            val bitmap = btm
            val bos = ByteArrayOutputStream()
            val bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return f
        } catch (e: IOException) { return null }
    }

    fun getBitmapMaskImage(maskedImage: Bitmap, mask: Bitmap, isInvert: Boolean): Bitmap
    {
        val c = Canvas()
        val result = Bitmap.createBitmap(maskedImage.width, maskedImage.height, Bitmap.Config.ARGB_8888)
        c.setBitmap(result)
        c.drawBitmap(maskedImage, 0f, 0f, null)
        val paint = Paint()
        paint.isFilterBitmap = false

        if (isInvert)
        {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }
        else
        {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
        c.drawBitmap(mask, 0f, 0f, paint)
        paint.xfermode = null
        return result

    }

    fun getBitmapInvertImage(src: Bitmap): Bitmap
    {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int
        val height = src.height
        val width = src.width
        for (y in 0 until height)
        {
            for (x in 0 until width)
            {
                pixelColor = src.getPixel(x, y)
                A = Color.alpha(pixelColor)
                R = 255 - Color.red(pixelColor)
                G = 255 - Color.green(pixelColor)
                B = 255 - Color.blue(pixelColor)
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }

    fun mergeImages(images: ArrayList<Any>, sizeX: Int, sizeY: Int, scale: Double, rects: ArrayList<Rect>?): Bitmap?
    {
        var bitmap: Bitmap? = null
        try
        {
            bitmap = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_8888)
            val c = Canvas(bitmap!!)
            var rec: Rect?
            for (i in images.indices) {
                val image = images[i]
                if (image.javaClass == Bitmap::class.java)
                {
                    val btm = image as Bitmap
                    val cx = btm.width
                    val cy = btm.height
                    rec = null
                    rects?.let {
                        try {rec = it[i]}
                        catch (e: IndexOutOfBoundsException){ rec = null}
                    } ?: run{
                        rec = CommonUtil.getEqualRatioRect(Rect(0, 0, cx, cy), sizeX, sizeY, true, 0, 0)
                    }
                    rec?.let{ c.drawBitmap(resizeImage(btm, it.width(), it.height()), it.left.toFloat(), it.top.toFloat(), null)}
                }
                else if (image.javaClass == ImageView::class.java)
                {
                    val imgView = image as ImageView
                    val layout = imgView.layoutParams as FrameLayout.LayoutParams
                    val imageB = image.drawable as BitmapDrawable
                    val btm = imageB.bitmap
                    val rtX = imgView.width.toDouble() * scale
                    val rtY = imgView.height.toDouble() * scale
                    val mdX = layout.leftMargin.toDouble() * scale
                    val mdY = layout.topMargin.toDouble() * scale
                    c.drawBitmap(resizeImage(btm, Math.round(rtX).toInt(), Math.round(rtY).toInt()), Math.round(mdX).toInt().toFloat(), Math.round(mdY).toInt().toFloat(), null)
                }
            }
        }
        catch (e: Exception)
        {
            return null
        }
        return bitmap

    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, roundPx: Float): Bitmap
    {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }


    fun getBitmapModifyExifInterface(imagePath: String): Bitmap
    {
        val exif: ExifInterface
        var image = BitmapFactory.decodeFile(imagePath)
        try { exif = ExifInterface(imagePath) } catch (e: IOException) { return image }
        val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val exifDegree = GraphicUtil.exifOrientationToDegrees(exifOrientation)
        image = GraphicUtil.rotateImage(image, exifDegree)
        return image
    }

    fun getOrientationToDegreesModifyExifInterface(imagePath: String): Int {
        val exif: ExifInterface
        try { exif = ExifInterface(imagePath) } catch (e: IOException) { return 0 }
        val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return exifOrientationToDegrees(exifOrientation)

    }

    fun exifOrientationToDegrees(exifOrientation: Int): Int
    {
        when(exifOrientation)
        {
            ExifInterface.ORIENTATION_ROTATE_90 -> return 90
            ExifInterface.ORIENTATION_ROTATE_180 -> return 180
            ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            else -> return 0
        }
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

    fun cropBitmapImage(image: Bitmap?, range: Rect): Bitmap?
    {
        var image = image
        try {
            image = Bitmap.createBitmap(image!!, range.left, range.top, range.right, range.bottom)
        } catch (e: IllegalArgumentException) {
            image = null
        }
        return image
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
        val cr = context.getContentResolver()
        MediaStore.Images.Media.insertImage(cr, image, imageName, imagePath)

    }



}