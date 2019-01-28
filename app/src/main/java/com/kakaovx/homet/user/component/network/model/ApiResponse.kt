package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("total_count") private val count: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: ArrayList<Repository>
)