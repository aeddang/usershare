package com.kakaovx.homet.user.component.model

class ContentModel(val id: String?, val title: String?, val description: String?, val image_url: String?) {
    override fun toString(): String {
        return "ContentModel(id = $id\n" +
                " title = $title\n" +
                " description = $description\n" +
                " image_url = $image_url)"
    }
}