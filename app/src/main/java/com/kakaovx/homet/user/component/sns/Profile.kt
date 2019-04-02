package com.kakaovx.homet.user.component.sns

data class Profile(val id:String, val name:String?){
    var email:String = ""
    var age:Int = -1
    var imgPath:String? = null
    var birth :String = ""
    var birthYear = ""
    var phoneNumber = ""
    var gender = ""
}