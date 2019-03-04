package com.kakaovx.homet.user.component.model

class HomeIssueProgramModel(val home_issue_program_name: String?,
                             val home_issue_program_description: String?,
                             val home_issue_program_url: String?) {
    override fun toString(): String {
        return "HomeIssueProgramModel(home_issue_program_name=$home_issue_program_name\n" +
                " home_issue_program_description=$home_issue_program_description\n" +
                " home_issue_program_url=$home_issue_program_url)"
    }
}