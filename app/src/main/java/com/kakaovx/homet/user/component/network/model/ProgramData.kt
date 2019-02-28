package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class ProgramData(
    @SerializedName("program_id") val program_id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("body_parts") val body_parts: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("calorie") val calorie: String?,
    @SerializedName("play_time") val play_time: String?,
    @SerializedName("confirm_status") val confirm_status: String?,
    @SerializedName("is_display") val is_display: String?,
    @SerializedName("exercise_count") val exercise_count: String?,
    @SerializedName("provider_id") val provider_id: String?,
    @SerializedName("thumbnail_intro") val thumbnail_intro: String?,
    @SerializedName("thumbnail_preview") val thumbnail_preview: String?,
    @SerializedName("add_time") val add_time: String?,
    @SerializedName("modify_time") val modify_time: String?
) {
    override fun toString(): String {
        return "ProgramData(program_id=$program_id\n" +
                " title=$title\n" +
                " description=$description\n" +
                " body_parts=$body_parts\n" +
                " difficulty=$difficulty\n" +
                " calorie=$calorie\n" +
                " play_time=$play_time\n" +
                " confirm_status=$confirm_status\n" +
                " is_display=$is_display\n" +
                " exercise_count=$exercise_count\n" +
                " provider_id=$provider_id\n" +
                " thumbnail_intro=$thumbnail_intro\n" +
                " thumbnail_preview=$thumbnail_preview\n" +
                " add_time=$add_time\n" +
                " modify_time=$modify_time)"
    }
}