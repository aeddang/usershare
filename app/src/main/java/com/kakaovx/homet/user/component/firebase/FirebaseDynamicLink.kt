package com.kakaovx.homet.user.component.firebase

import android.app.Activity
import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.util.Log

class FirebaseDynamicLink( val activity:Activity ) {

    private var TAG = javaClass.simpleName
    private var delegate: Delegate? = null

    interface Delegate{
        fun onDetactedDeepLink(deepLink:Uri)
        fun onCreateDynamicLinkComplete(dynamicLink:String)
        fun onCreateDynamicLinkError()
    }
    fun setOnDynamicLinkListener( _delegate: Delegate? ){ delegate = _delegate }


    init {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(activity.intent)
            .addOnSuccessListener(activity) { pendingDynamicLinkData ->
                pendingDynamicLinkData?.let {
                    val deepLink = it.link
                    Log.d(TAG, "deepLink $deepLink")
                    delegate?.onDetactedDeepLink(deepLink)
                }
            }.addOnFailureListener(activity) { e ->
                Log.d(TAG, "getDynamicLink:onFailure", e)
            }
    }

    fun requestDynamicLink(title:String,image:String, desc:String, qurryString:String?){
        val longLink = "http://${activity.getString(R.string.fdl_domain)}/?$qurryString"
        val longLinkUri = Uri.parse(longLink)
        val prefix = "https://${activity.getString(R.string.fdl_link)}"
        Log.d(TAG, "longLinkUri ${longLinkUri.query}")
        Log.d(TAG, "prefix $prefix")

        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink( longLinkUri )
            .setDomainUriPrefix( prefix )
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder(activity.getString(R.string.app_id))
                    .build())

            .setSocialMetaTagParameters(DynamicLink.SocialMetaTagParameters.Builder()
                 .setTitle( title )
                 .setImageUrl( Uri.parse(image) )
                 .setDescription( desc )
                 .build())
                     /*
                    .setIosParameters(
                        DynamicLink.IosParameters.Builder("com.example.ios")
                            .setAppStoreId("123456789")
                            .setMinimumVersion("1.0.1")
                            .build())

                    .setGoogleAnalyticsParameters(
                        DynamicLink.GoogleAnalyticsParameters.Builder()
                            .setSource("orkut")
                            .setMedium("social")
                            .setCampaign("example-promo")
                            .build())
                    .setItunesConnectAnalyticsParameters(
                        DynamicLink.ItunesConnectAnalyticsParameters.Builder()
                            .setProviderToken("123456")
                            .setCampaignToken("example-promo")
                            .build())
                     */
            .buildShortDynamicLink()
            .addOnSuccessListener { result ->
                val shortLink = result.shortLink
                val flowchartLink = result.previewLink
                delegate?.onCreateDynamicLinkComplete("$prefix${shortLink.path}")
            }.addOnFailureListener { error->
                Log.d(TAG, "dynamicLink Error ${error.message}")
                delegate?.onCreateDynamicLinkError()
            }
    }
}

