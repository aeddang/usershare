package com.kakaovx.homet.user.component.model

class HomeRecommendModel(val home_recommend_name: String?,
                         val home_recommend_description: String?,
                         val home_recommend_url: String?) {
    override fun toString(): String {
        return "HomeRecommendModel(home_recommend_name=$home_recommend_name," +
                " home_recommend_description=$home_recommend_description," +
                " home_recommend_url=$home_recommend_url)"
    }
}