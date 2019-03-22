package com.kakaovx.homet.user.component.network.model

import com.google.gson.annotations.SerializedName

data class TrainerMotionData(
    @SerializedName("keypoints") val key_points: List<TrainerPoseData>
)