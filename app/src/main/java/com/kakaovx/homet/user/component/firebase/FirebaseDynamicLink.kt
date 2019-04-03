package com.kakaovx.homet.user.component.firebase

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class FirebaseDynamicLink{

    fun getDynamicLink(){
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://www.example.com/"))
            .setDomainUriPrefix("https://example.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())
            .buildDynamicLink()

        val dynamicLinkUri = dynamicLink.uri

    }



}