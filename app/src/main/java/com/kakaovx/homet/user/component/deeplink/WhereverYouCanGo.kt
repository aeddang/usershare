package com.kakaovx.homet.user.component.deeplink

import android.net.Uri
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log

import org.json.JSONException
import org.json.JSONObject

data class IwillGo(var pageID:PageID? = null , var param:Map<String, Any>? = null, var isPopup:Boolean = false){
    private var TAG = javaClass.simpleName

    companion object {
        private const val PAGE_KEY = "pageID"
        private const val IS_POPUP_KEY = "isPopup"
    }

    fun stringfy():String{

        val json = if (param!= null) JSONObject(param) else JSONObject()
        try {
            pageID?.let { json.put(PAGE_KEY, it.resId) }
            json.put(IS_POPUP_KEY, isPopup)
        }catch(e:JSONException){
            Log.d(TAG, "json error $e")
        }
        return  json.toString()
    }

    fun parse(jsonString:String): IwillGo {
        val json = JSONObject(jsonString)
        val getParam = HashMap<String, Any>()
        json.keys().forEach { key->
            when (key) {
                PAGE_KEY -> {
                    val findID = json.getInt(PAGE_KEY)
                    pageID = PageID.values().find { it.resId == findID }
                }
                IS_POPUP_KEY -> isPopup = json.getBoolean( IS_POPUP_KEY )
                else -> getParam[key] = json.get(key)
            }
        }
        param = getParam
        return this
    }

    fun qurryString():String{
        pageID ?: return ""
        val qurry = "$PAGE_KEY=${pageID!!.resId}"
        param ?: return qurry
        return param!!.keys.fold(qurry){ sum, key -> "$sum&$key=${param!![key]}" }
    }

    fun parseQurryString(qurry:String):IwillGo{
        val fullPath = "https://kkk.com?$qurry"
        val uri=  Uri.parse(fullPath)
        val getParam = HashMap<String, Any>()
        uri.queryParameterNames.forEach { key->
            val value = uri.getQueryParameter(key)
            value?.let { v->
                when (key) {
                    PAGE_KEY -> {
                        val findID = v.toInt()
                        pageID = PageID.values().find { it.resId == findID }
                    }
                    IS_POPUP_KEY -> isPopup = v.toBoolean()
                    else -> getParam[key] = v
                }
            }
        }
        param = getParam
        return this
    }
}

object WhereverYouCanGo{
    fun parseQurryIwillGo(qurry:String): IwillGo {
        return IwillGo().parseQurryString(qurry)
    }

    fun parseJsonIwillGo(jsonString:String): IwillGo {
        return IwillGo().parse(jsonString)
    }

    fun stringfyQurryIwillGo(pageID:PageID? , param:Map<String, Any>? = null, isPopup:Boolean = false):String{
        return IwillGo(pageID, param, isPopup).qurryString()
    }

    fun stringfyIwillGo(pageID:PageID? , param:Map<String, Any>? = null, isPopup:Boolean = false):String{
        return IwillGo(pageID, param, isPopup).stringfy()
    }
}