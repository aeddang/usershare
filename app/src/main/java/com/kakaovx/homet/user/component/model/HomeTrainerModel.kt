package com.kakaovx.homet.user.component.model

class HomeTrainerModel(val home_trainer_name: String?,
                       val home_trainer_description: String?,
                       val home_trainer_job: String?,
                       val home_trainer_url: String?) {
    override fun toString(): String {
        return "HomeTrainerModel(home_trainer_name=$home_trainer_name," +
                " home_trainer_description=$home_trainer_description," +
                " home_trainer_job=$home_trainer_job," +
                " home_trainer_url=$home_trainer_url)"
    }
}