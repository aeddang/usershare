package lib.util


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Uri
import android.text.InputFilter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern





object CommonUtil
{

    fun isDeviceOnline(context: Context):Boolean
    {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun readTextFileFromRawResource(context: Context,
                                    resourceId: Int): String? {
        val inputStream = context.resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var nextLine: String
        val body = StringBuilder()

        try
        {
            nextLine = bufferedReader.readLine()
            while (nextLine != null)
            {
                body.append(nextLine)
                body.append('\n')
                nextLine = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            return null
        }

        return body.toString()
    }

    fun getTimeStr(t: Int, div: String): String
    {
        val m = t % 3600
        var tim = ""
        if (Math.floor((t.toFloat() / 3600f).toDouble()) > 0) tim = intToText(Math.floor((t.toFloat() / 3600f).toDouble()).toInt(), 2) + div
        tim = tim + intToText(Math.floor((m.toFloat() / 60f).toDouble()).toInt(), 2) + div + intToText(m % 60, 2)
        return tim
    }


    fun getPathFromUri(context: Context, uri: Uri): String {
        var path = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null)
        {
            cursor.moveToNext()
            path = cursor.getString(cursor.getColumnIndex("_data"))
            cursor.close()
        }

        return path
    }


    fun getMarketUrl(context: Context): String {
        return "market://details?id=" + context.packageName
    }

    fun goLink(context: Context, link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        val u = Uri.parse(link)
        i.data = u
        context.startActivity(i)
    }

    fun getRestrictSpecialInput(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[0-9가-힣ㄱ-ㅎㅏ-ㅣ\u318D\u119E\u11A2\u2025a-zA-Z]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else null
        }

    }

    fun getOnlyEngInput(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-zA-Z0-9]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else null
        }
    }

    fun getOnlyNum(str: String?): String {
        if (str == null) return ""
        val sb = StringBuffer()
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {
                sb.append(str[i])
            } else if (str[i] == "."[0]) {

                sb.append(str[i])
            } else if (str[i] == "-"[0]) {

                sb.append(str[i])
            }
        }
        return sb.toString()
    }


    fun getColorArray(div: Int): IntArray {
        val size = div * 4
        val colorA = IntArray(size)
        val dt = 1 / (div - 1).toFloat()
        var d = 0f

        for (i in 0 until size) {
            var r = 0
            var g = 0
            var b = 0

            val df = (i % div).toFloat()
            d = 255f * df * dt

            val line = Math.floor((i / div).toDouble()).toInt()
            when (line) {
                0 -> {
                    r = Math.round(d)
                    g = Math.round(d)
                    b = Math.round(d)
                }
                1 -> {
                    r = 255
                    g = 0

                    b = Math.round(d)
                }
                2 -> {
                    r = Math.round(d)
                    g = 255
                    b = 0
                }
                3 -> {
                    r = 0
                    g = Math.round(d)
                    b = 255
                }
            }

            colorA[i] = Color.rgb(r, g, b)
        }
        return colorA
    }

    fun clearApplicationCache(context: Context, _dir: java.io.File?)
    {
        var dir = _dir
        if (dir == null) dir = context.cacheDir
        if (dir == null) return
        val children = dir.listFiles()
        try {
            for (child in children)
                if (child.isDirectory) clearApplicationCache(context, child) else child.delete()
        }
        catch (e: Exception)
        {
        }
    }

    fun getPriceStr(number: Float): String {
        return getPriceStr(number, 1)
    }

    fun getPriceStr(number: Float, l: Int): String {
        var str = if(l == 0) String.format("%f", number) else String.format("%." + l + "f", number)
        val strA = str.split(Pattern.quote(".").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var pointStr = ""
        if (strA.size > 1)
        {
            pointStr = strA[1]
            pointStr =  if (Integer.parseInt(pointStr) != 0) ".$pointStr" else  ""
            str = strA[0]
        }

        val num = str.length
        var s = ""
        if (num > 3)
        {
            val min = num - 3
            var sliceS: String
            var i = min
            while (i > -3)
            {
                if (i > 0)
                {
                    sliceS = str.substring(i, i + 3)
                    s = ",$sliceS$s"
                }
                else
                {
                    sliceS = str.substring(0, 3 + i)
                    s = sliceS + s
                }
                i -= 3
            }
        }
        else
        {
            s = str
        }
        return s + pointStr
    }

    fun getCurrentTimeCode(): String {
        val now = System.currentTimeMillis()
        val dat = Date(now)
        val dateNow = SimpleDateFormat("yyyyMMddHHmmss")
        return dateNow.format(dat)
    }

    fun getUniqueNameByKey(key: String): String {
        return key + getCurrentTimeCode() + Math.random()
    }

    fun getQurry(qurryString: String): Map<String, String>?
    {
        var qurryA = qurryString.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (qurryA.size < 2) return null
        qurryA = qurryA[1].split("\\&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (qurryA.size < 1) return null

        val qurry = HashMap<String, String>()
        for (i in qurryA.indices)
        {
            val q = qurryA[i].split("\\=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (q.size >= 2) qurry[q[0]] = q[1]
        }
        return qurry
    }

    fun intToText(n: Int, len: Int): String
    {
        var str = n.toString()
        val num = str.length
        if (num < len)
        {
            for (i in num until len)
            {
                str = "0$str"
            }
        }
        return str
    }

    fun getDateByCode(yymmdd: Int, key: String): String
    {
        val ymdStr = yymmdd.toString()
        if (ymdStr.length != 8) return ""
        val str = ymdStr.substring(0, 4) + key + ymdStr.substring(4, 6) + key + ymdStr.substring(6, 8)
        return str
    }

    fun getDays(year: Int, mon: Int, day: Int): Int
    {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, mon - 1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        val dateRepresentation = cal.time
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    fun getDaysInMonth(year: Int, mon: Int): Int
    {
        val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (isLeapYear(year) == true) daysInMonth[1] = 29
        return daysInMonth[mon - 1]
    }

    fun getRandomInt(range: Int): Int
    {
        var idx = Math.floor(Math.random() * range).toInt()
        if (idx == range) idx = range - 1
        return idx
    }

    fun getRandomInt(range: Int, except: Int): Int
    {
        var r = getRandomInt(range - 1)
        if (r >= except) r++
        return r
    }

    fun getRandomInt(range: Int, exceptA: ArrayList<String>): Int
    {
        val numA = ArrayList<String>()
        for (i in 0 until range)
        {
            val idx = i.toString()
            if (exceptA.indexOf(idx) == -1) numA.add(idx)
        }
        val r = getRandomInt(numA.size)
        return Integer.parseInt(numA[r])
    }


    private fun isLeapYear(year: Int): Boolean
    {
        return if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) true else false
    }

    fun getColorByString(cororCode: String): FloatArray
    {
        val cd = cororCode.replace("#", "")
        if (cd.length != 6) {
            val returnData = floatArrayOf(0f, 0f, 0f, 0f)
            return returnData
        }
        val cr0 = cd.substring(0, 1)
        val cr1 = cd.substring(1, 2)
        val cg0 = cd.substring(2, 3)
        val cg1 = cd.substring(3, 4)
        val cb0 = cd.substring(4, 5)
        val cb1 = cd.substring(5, 6)
        val r0 = getNumberByCode16(cr0)
        val r1 = getNumberByCode16(cr1)
        val g0 = getNumberByCode16(cg0)
        val g1 = getNumberByCode16(cg1)
        val b0 = getNumberByCode16(cb0)
        val b1 = getNumberByCode16(cb1)
        val r = (r0 * 16f + r1) / 256f
        val g = (g0 * 16f + g1) / 256f
        val b = (b0 * 16f + b1) / 256f
        val returnData = floatArrayOf(r, g, b, 1.0f)
        return returnData
    }

    fun getNumberByCode16(code: String): Float
    {

        val cd = code.toUpperCase()
        var n = when(cd)
        {
            "A" -> 10f
            "B" -> 11f
            "C" -> 12f
            "D" -> 13f
            "E" -> 14f
            "F" -> 15f
            else -> Integer.parseInt(code).toFloat()
        }
        return n
    }

    fun getEqualRatioRect(mc: Rect, tw: Int, th: Int, smallResize: Boolean?, dfWid: Int, dfHei: Int): Rect
    {
        var dfWid = dfWid
        var dfHei = dfHei
        var w: Int
        var h: Int
        if (dfWid == 0)
        {
            w = mc.right
            dfWid = w
        }
        else
        {
            w = dfWid
        }
        if (dfHei == 0)
        {
            h = mc.bottom
            dfHei = h
        }
        else
        {
            h = dfHei
        }

        var p: Int
        if (w > tw || h > th)
        {
            if (w > tw)
            {
                p = w
                w = tw
                h = Math.floor((h * (tw.toFloat() / p.toFloat())).toDouble()).toInt()
            }
            if (h > th)
            {
                p = h
                h = th
                w = Math.floor((w * (th.toFloat() / p.toFloat())).toDouble()).toInt()
            }
        }
        else
        {
            if (smallResize == true)
            {
                if (w < tw)
                {
                    p = w
                    w = tw
                    h = Math.floor((h * (tw.toFloat() / p.toFloat())).toDouble()).toInt()
                }
                if (h > th)
                {
                    p = h
                    h = th
                    w = Math.floor((w * (th.toFloat() / p.toFloat())).toDouble()).toInt()
                }
            }
        }

        mc.left = Math.round(((tw - w) / 2).toFloat())
        mc.top = Math.round(((th - h) / 2).toFloat())
        mc.bottom = mc.top + h
        mc.right = mc.left + w
        return mc
    }
}