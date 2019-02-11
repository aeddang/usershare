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
            PageID.HOME -> { PageHome() }
            PageID.CONTENT -> {
                val param = HashMap<String, Any>()
                param[ParamType.PAGES.key] = arrayOf(PageID.PROGRAM, PageID.FREE_WORKOUT, PageID.TRAINER)
                PageContent().setParam(param)
            }
            PageID.PLANNER -> { PagePlanner() }
            PageID.SEARCH -> { PageSearch() }
            PageID.PROFILE -> { PageProfile() }
            PageID.PROGRAM -> { PageProgram() }
            PageID.FREE_WORKOUT -> { PageFreeWorkout() }
            PageID.TRAINER -> { PageTrainer() }
        }
    }
}

enum class PageID(val resId: Int) {
    SPLASH(R.string.company),
    HOME(R.string.page_home),
    CONTENT(R.string.page_content),
    PLANNER(R.string.page_planner),
    SEARCH(R.string.page_search),
    PROFILE(R.string.page_profile),
    PROGRAM(R.string.page_program),
    FREE_WORKOUT(R.string.page_free_workout),
    TRAINER(R.string.page_trainer)
}

enum class ParamType(val key:String) {
    PAGES("pages")
}
