package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T> (
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<T>
) {
    override fun toString(): String {
        return "ApiResponse(code='$code'\n" +
                " message='$message'\n" +
                " data=$data)"
    }
}