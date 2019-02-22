package com.kakaovx.homet.user.component.model

class HomeProgramModel(val home_program_name: String?,
                       val home_program_description: String?,
                       val home_program_url: String?) {
    override fun toString(): String {
        return "HomeProgramModel(home_program_name=$home_program_name," +
                " home_program_description=$home_program_description," +
                " home_program_url=$home_program_url)"
    }
}