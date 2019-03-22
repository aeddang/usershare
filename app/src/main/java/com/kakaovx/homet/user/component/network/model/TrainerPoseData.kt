package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class TrainerPoseData(
    @SerializedName("timestamp") val time_stamp: Float,
    @SerializedName("posevector") val pose_vector: List<Float>
) {
    override fun toString(): String {
        return "TrainerPoseData(time_stamp=$time_stamp\n" +
               " pose_vector=$pose_vector)"
    }
}