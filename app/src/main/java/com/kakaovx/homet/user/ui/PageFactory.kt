package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.ui.page.*
import com.kakaovx.homet.user.ui.page.PageSplash

class PageFactory {

    companion object {
        private  var currentInstance:PageFactory? = null
        fun getInstance():PageFactory {
            if(currentInstance == null) currentInstance = PageFactory()
            return currentInstance!!
        }
    }

    init {
        PageFactory.currentInstance = this
    }


    fun getPageByID(id:PageID): PageFragment {
        return when(id) {
            PageID.SPLASH -> { PageSplash() }
            PageID.MAIN -> { PageMain() }
            PageID.SUB -> { PageSub() }
            PageID.NETWORK -> { PageNetworkTest() }
            PageID.TEST -> {
                val param = HashMap<String,Any>()
                param[ParamType.PAGES.key] = arrayOf(PageID.NETWORK,PageID.SUB,PageID.POPUP_TEST)
                PageViewPager().setParam(param)}

            PageID.POPUP_TEST -> { PopupTest() }
            else -> { PageMain() }
        }
    }
}

enum class PageID(val title:String) {
    SPLASH("splash"),
    MAIN("home"),
    SUB("sub"),
    NETWORK("network"),
    TEST("test"),
    POPUP_TEST("popuptest")
}

enum class ParamType(val key:String) {
    PAGES("pages")
}
