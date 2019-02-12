package com.kakaovx.homet.user.component.network.model
import com.google.gson.annotations.SerializedName

data class ResultData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("owner") val ownerData: OwnerData?,
    @SerializedName("description") val description: String?
)

data class OwnerData(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("grAvatar_id") val grAvatarId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("received_events_url") val receivedEventsUrl: String?,
    @SerializedName("type") val type: String?
)