package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePosition
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
            PageID.CONTENT_DETAIL -> { PageContentDetail() }
            PageID.TEST -> { PageTest() }
            PageID.TEST_POP -> { PageDividedGestureTest() }
        }
    }
}

enum class PageID(val resId: Int, override var position: Int = 9999):PagePosition {
    HOME(R.string.page_home),
    CONTENT(R.string.page_content,1),
    PLANNER(R.string.page_planner,2),
    SEARCH(R.string.page_search,3),
    PROFILE(R.string.page_profile,4),
    PROGRAM(R.string.page_program,5),
    FREE_WORKOUT(R.string.page_free_workout,6),
    TRAINER(R.string.page_trainer,7),
    CONTENT_DETAIL(R.string.page_content,8),
    TEST(1000000000,9),
    TEST_POP(1000000002)
}

enum class ParamType(val key:String) {
    PAGES("pages"),
    DETAIL("detail")
}
