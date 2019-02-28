package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class TrainerData(
    @SerializedName("provider_id") val provider_id: String?,
    @SerializedName("provider_name") val provider_name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("trainer_alias") val trainer_alias: String?,
    @SerializedName("thumbnail_provider_profile") val thumbnail_provider_profile: String?
) {
    override fun toString(): String {
        return "TrainerData(provider_id=$provider_id\n" +
                " provider_name=$provider_name\n" +
                " description=$description\n" +
                " trainer_alias=$trainer_alias\n" +
                " thumbnail_provider_profile=$thumbnail_provider_profile)"
    }
}