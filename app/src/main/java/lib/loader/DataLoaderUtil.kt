package lib.loader

import android.util.Log
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

internal object DataLoaderUtil
{

    fun setValue(key: String, value: String): String
    {
        return ("Content-Disposition: form-data; name=\"" + key + "\"\r\n" + value)
    }


    fun setFile(key: String, fileName: String): String
    {
        return ("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n")
    }

    fun setFileBuffer(file: FileObject, wr: DataOutputStream, delimiter: String)
    {

        var ism: FileInputStream? = null
        try {
            ism = FileInputStream(file.file)
        }
        catch (e: FileNotFoundException)
        {
            Log.d(DataManager.TAG, "DataLoader Error FileNotFoundException ")
            return
        }

        // 파일 복사 작업 시작

        try {

            var bufferSize = 0
            wr.writeBytes(delimiter) // 반드시 작성해야 한다.
            val cond = setFile(file.key, file.name)
            wr.writeBytes(cond)
            Log.i(DataManager.TAG, "cond : $cond")
            wr.writeBytes("\r\n")
            bufferSize = ism!!.available()
            val buffer = ByteArray(bufferSize)
            Log.i(DataManager.TAG, "buffer : " + buffer.size)
            while (ism!!.read(buffer) != -1) wr.write(buffer, 0, ism!!.read(buffer))
            ism?.close()
        }
        catch (e: IOException)
        {
            Log.d(DataManager.TAG, "DataLoader Error File write fail")
        }


    }

    fun getParamsString(urlParam: Map<String, String>, delimiter: String): String
    {
        val sb = StringBuffer()
        val keySet = urlParam.keys.toTypedArray()
        var key: String

        for (key in keySet)
        {
            sb.append(delimiter)
            sb.append(setValue(key, urlParam[key]!!))

        }
        return sb.toString()
    }


}
