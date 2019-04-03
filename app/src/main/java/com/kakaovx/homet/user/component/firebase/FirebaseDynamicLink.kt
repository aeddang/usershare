package com.kakaovx.homet.user.component.firebase

import android.app.Activity
import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.kakaovx.homet.user.util.Log

class FirebaseDynamicLink( val activity:Activity ) {
    companion object {
        private const val DOMAIN = "https://www.kakaovx.com/"
    }
    private var TAG = javaClass.simpleName

    init {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(activity.intent)
            .addOnSuccessListener(activity) { pendingDynamicLinkData ->
               var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
            }.addOnFailureListener(activity) { e -> Log.d(TAG, "getDynamicLink:onFailure", e) }
    }


    fun getDynamicLink(url:String){
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://www.example.com/"))
            .setDomainUriPrefix("https://example.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())
            .buildDynamicLink()

        //getShortLink( dynamicLink.uri )

    }

    fun getShortLink(){
        val shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://www.example.com/"))
            .setDomainUriPrefix("https://example.page.link")
            .buildShortDynamicLink()
            .addOnSuccessListener { result ->
                // Short link created
                val shortLink = result.shortLink
                val flowchartLink = result.previewLink
            }.addOnFailureListener {
                // Error
                // ...
            }
    }



}