package lib.loader

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FileObject : Any() {

    var key: String
    var name: String
    var file: File? = null

    init {
        key = ""
        name = ""
        file = null
    }

    fun addUriFile(uri: Uri)
    {
        file = File(uri.path)
    }

    fun addBitmapFile(bitmap: Bitmap, format: Bitmap.CompressFormat)
    {

        var outStream: OutputStream? = null

        var fileName = "uploadFile"
        if (format == Bitmap.CompressFormat.JPEG)
        {
            fileName = "$fileName.jpg"
        }
        else if (format == Bitmap.CompressFormat.PNG)
        {
            fileName = "$fileName.png"
        }
        file = File(Environment.getExternalStorageDirectory().toString() + "/", fileName)
        if(file == null) return
        Log.i(DataManager.TAG, file!!.name)
        try
        {
            outStream = FileOutputStream(file!!) as OutputStream
            bitmap.compress(format, 100, outStream)
            outStream.flush()
            outStream.close()
            Log.d(DataManager.TAG, "SAVE IMAGE SUCCESS : " + file!!.length())
        }
        catch (e: Exception)
        {
            Log.d(DataManager.TAG, "SAVE IMAGE FAIL" + e.toString())
        }

    }


}
