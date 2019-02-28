package com.kakaovx.homet.user.component.network.model
import com.google.gson.annotations.SerializedName

data class ResultData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("owner") val ownerData: OwnerData?,
    @SerializedName("description") val description: String?
) {
    override fun toString(): String {
        return "ResultData(id=$id\n" +
                " name=$name\n" +
                " fullName=$fullName\n" +
                " ownerData=$ownerData\n" +
                " description=$description)"
    }
}






