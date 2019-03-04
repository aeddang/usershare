package com.kakaovx.homet.user.component.model

class HomeFreeWorkoutModel(val home_free_workout_description: String?,
                           val home_free_workout_title: String?,
                           val home_free_workout_url: String?) {
    override fun toString(): String {
        return "HomeFreeWorkoutModel(home_free_workout_description=$home_free_workout_description\n" +
                " home_free_workout_title=$home_free_workout_title\n" +
                " home_free_workout_url=$home_free_workout_url)"
    }
}