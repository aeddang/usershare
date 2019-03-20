package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class WorkoutData(
    @SerializedName("exercise_id") val exercise_id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("body_parts") val body_parts: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("calorie") val calorie: String?,
    @SerializedName("play_time") val play_time: String?,
    @SerializedName("motion_count") val motion_count: String?,
    @SerializedName("program_count") val program_count: String?,
    @SerializedName("confirm_status") val confirm_status: String?,
    @SerializedName("is_display") val is_display: String?,
    @SerializedName("read_count") val read_count: String?,
    @SerializedName("provider_id") val provider_id: String?,
    @SerializedName("add_time") val add_time: String?,
    @SerializedName("modify_time") val modify_time: String?,
    @SerializedName("movie_url") val movie_url: String?,
    @SerializedName("thumb_url") val thumb_url: String?,
    @SerializedName("free_motion_id") val free_motion_id: String?,
    @SerializedName("free_motion_movie_url") val free_motion_movie_url: String?,
    @SerializedName("free_motion_thumb_url") val free_motion_thumb_url: String?
){
    override fun toString(): String {
        return "WorkoutData(exercise_id=$exercise_id\n" +
                " title=$title\n" +
                " description=$description\n" +
                " body_parts=$body_parts\n" +
                " difficulty=$difficulty\n" +
                " calorie=$calorie\n" +
                " play_time=$play_time\n" +
                " motion_count=$motion_count\n" +
                " program_count=$program_count\n" +
                " confirm_status=$confirm_status\n" +
                " is_display=$is_display\n" +
                " read_count=$read_count\n" +
                " provider_id=$provider_id\n" +
                " add_time=$add_time\n" +
                " modify_time=$modify_time\n" +
                " movie_url=$movie_url\n" +
                " thumb_url=$thumb_url\n" +
                " free_motion_id=$free_motion_id\n" +
                " free_motion_movie_url=$free_motion_movie_url\n" +
                " free_motion_thumb_url=$free_motion_thumb_url)"
    }
}