package com.kakaovx.homet.user.component.model

class ContentModel(val title: String?, val description: String?, val image_url: String?) {
    override fun toString(): String {
        return "ContentModel(title = $title\n" +
                " description = $description\n" +
                " image_url = $image_url)"
    }
}