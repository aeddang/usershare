package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class Response<T> (
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T
) {
    override fun toString(): String {
        return "Response(code='$code'\n" +
                " message='$message'\n" +
                " data=$data)"
    }
}