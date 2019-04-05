package com.kakaovx.homet.user.component.deeplink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.firebase.FirebaseDynamicLink
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log


class DeepLinkManager(val activity:Activity): FirebaseDynamicLink.Delegate{
    private var TAG = javaClass.simpleName
    private var dynamicLink:FirebaseDynamicLink = FirebaseDynamicLink(activity)
    init {
        dynamicLink.setOnDynamicLinkListener( this )
    }

    override fun onDetactedDeepLink(deepLink: Uri){
        Log.d(TAG, "onDetactedDeepLink ${deepLink.query}")
        goQurryPage(deepLink.query)
    }


    override fun onCreateDynamicLinkComplete(dynamicLink:String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, dynamicLink)
        shareIntent.type = "text/plain"
        activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.title_share)))
    }

    override fun onCreateDynamicLinkError(){

    }

    fun sendSns(title:String,image:String, desc:String, pageID:PageID? = null, param:Map<String, Any>? = null, isPopup:Boolean = false){
        val qurryString = WhereverYouCanGo.stringfyQurryIwillGo(pageID, param, isPopup)
        dynamicLink.requestDynamicLink(title,image, desc, qurryString)
    }

    fun goQurryPage(qurry:String?){
        qurry ?: return
        val iwillGo = WhereverYouCanGo.parseQurryIwillGo(qurry)
        goPage(iwillGo)
    }

    fun goLinkPage(link:String?){
        link ?: return
        val iwillGo = WhereverYouCanGo.parseJsonIwillGo(link)
        goPage(iwillGo)
    }

    fun goPage(iwillGo:IwillGo){
        Log.d(TAG, "goPage $iwillGo")
        iwillGo.pageID?.let {
            if(iwillGo.isPopup) PagePresenter.getInstance<PageID>().openPopup(it, iwillGo.param)
            else PagePresenter.getInstance<PageID>().pageChange(it, iwillGo.param) }
    }

}

