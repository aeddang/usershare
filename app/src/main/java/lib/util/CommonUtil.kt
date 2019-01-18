package lib.util


import android.content.Context
import android.net.ConnectivityManager

object CommonUtil
{
    fun isDeviceOnline(context: Context):Boolean
    {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}