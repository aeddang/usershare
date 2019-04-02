package com.kakaovx.homet.user.component.sns

import android.content.Intent

interface Sns {
    fun create()
    fun destroy()
    fun login()
    fun signUp()
    fun logout()
    fun requestProfile()
    fun getAccessTokenID():String?
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?):Boolean
}

enum class SnsType(val idx: Int){
    Kakao(0),
    FaceBook(1),
    ALL(-1)
}

enum class SnsStatus(val progress: Int){
    Logout(0),
    Login(1),
    Signup(2)
}

enum class SnsError(var data:Any?){
    Session(null),
    Server(null),
    Client(null),
    Network(null)
}

