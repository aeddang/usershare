package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.ui.page.PageMain
import com.kakaovx.homet.user.ui.page.PageSub
import com.kakaovx.homet.user.ui.page.PageViewPager
import com.kakaovx.homet.user.ui.page.PopupTest

class PageFactory{

    companion object {
        private  var currentInstence:PageFactory? = null
        fun getInstence():PageFactory {
            if(currentInstence == null) currentInstence = PageFactory()
            return currentInstence!!
        }
    }

    init {
        PageFactory.currentInstence = this
    }


    fun getPageByID(id:PageID): PageFragment {
        return when(id) {
            PageID.MAIN -> { PageMain() }
            PageID.SUB -> { PageSub() }
            PageID.TEST -> {
                val param = HashMap<String,Any>()
                param[ParamType.PAGES.key] = arrayOf(PageID.SUB,PageID.SUB,PageID.SUB)
                PageViewPager().setParam(param)}
            PageID.POPUP_TEST -> { PopupTest() }
            else -> { PageMain() }
        }
    }
}

enum class PageID(val title:String) {
    MAIN("home"),
    SUB("sub"),
    TEST("test"),
    POPUP_TEST("popuptest")
}

enum class ParamType(val key:String) {
    PAGES("pages")
}
