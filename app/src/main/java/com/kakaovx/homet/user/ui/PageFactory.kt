package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.ui.page.*

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
            PageID.HOME -> { PageMain() }
            PageID.PROGRAM -> { PageSub() }
            PageID.PLANNER -> { PageNetworkTest() }
            PageID.SEARCH -> {
                val param = HashMap<String,Any>()
                param[ParamType.PAGES.key] = arrayOf(PageID.PROGRAM, PageID.PLANNER, PageID.SEARCH)
                PageViewPager().setParam(param)
            }
            PageID.PROFILE -> { PopupTest() }
        }
    }
}

enum class PageID(val resId: Int) {
    SPLASH(R.string.company),
    HOME(R.string.tab_home),
    PROGRAM(R.string.tab_program),
    PLANNER(R.string.tab_planner),
    SEARCH(R.string.tab_search),
    PROFILE(R.string.tab_profile)
}

enum class ParamType(val key:String) {
    PAGES("pages")
}
