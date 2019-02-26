package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)