package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.ui.page.*

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

            PageID.SUB -> { PageNetworkTest() }

            PageID.TEST -> {
                val param = HashMap<String,Any>()
                param[ParamType.PAGES.key] = arrayOf(PageID.SUB,PageID.SUB,PageID.SUB)
                PageViewPager().setParam(param)}

            PageID.POPUP_TEST -> { PopupTest() }

            else -> { PageMain() }
        }
    }
}

enum class PageID {
    MAIN,SUB,TEST,
    POPUP_TEST
}

enum class ParamType(val key:String) {
    PAGES("pages")
}
