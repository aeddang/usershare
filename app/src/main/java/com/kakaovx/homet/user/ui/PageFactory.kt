package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePosition
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.ui.page.*
import com.kakaovx.homet.user.ui.page.content.PageContent
import com.kakaovx.homet.user.ui.page.content.PageHome
import com.kakaovx.homet.user.ui.page.content.program.PageProgram
import com.kakaovx.homet.user.ui.page.content.search.PageFilter
import com.kakaovx.homet.user.ui.page.content.trainer.PageTrainer
import com.kakaovx.homet.user.ui.page.content.workout.PageFreeWorkout
import com.kakaovx.homet.user.ui.page.content.workout.detail.PageContentDetail
import com.kakaovx.homet.user.ui.page.planner.program.PagePlanner
import com.kakaovx.homet.user.ui.page.player.PopupCamera
import com.kakaovx.homet.user.ui.page.player.PopupPlayer
import com.kakaovx.homet.user.ui.page.profile.PageProfile
import com.kakaovx.homet.user.ui.page.content.search.PageSearch
import com.kakaovx.homet.user.ui.page.etc.account.PageAccount
import com.kakaovx.homet.user.ui.page.etc.payment.PagePayment
import com.kakaovx.homet.user.ui.page.planner.diet.PageDietPlanner
import com.kakaovx.homet.user.ui.page.profile.setting.PageSetting
import com.kakaovx.homet.user.ui.page.report.diet.PageDietReport

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
            PageID.HOME -> PageHome()
            PageID.CONTENT -> {
                val param = HashMap<String, Any>()
                param[ParamType.PAGES.key] = arrayOf(PageID.PROGRAM, PageID.FREE_WORKOUT, PageID.TRAINER)
                PageContent().setParam(param)
            }
            PageID.PROGRAM_PLAN -> PagePlanner()
            PageID.SEARCH -> PageSearch()
            PageID.SEARCH_FILTER -> PageFilter()
            PageID.PROFILE -> PageProfile()
            PageID.PROGRAM -> PageProgram()
            PageID.FREE_WORKOUT -> PageFreeWorkout()
            PageID.TRAINER -> PageTrainer()
            PageID.CONTENT_DETAIL -> PageContentDetail()
            PageID.POPUP_PLAYER -> PopupPlayer()
            PageID.POPUP_CAMERA -> PopupCamera()
            PageID.ACCOUNT -> PageAccount()
            PageID.PAYMENT -> PagePayment()
            PageID.DIET_PLAN -> PageDietPlanner()
            PageID.SETTING -> PageSetting()
            PageID.REPORT_DIET -> PageDietReport()
            PageID.REPORT_PROGRAM -> PageProgram()
            PageID.TEST -> PageTest()
            PageID.TEST_POP -> PageDividedGestureTest()
        }
    }
}

enum class PageID(val resId: Int, override var position: Int = 9999):PagePosition {
    HOME(R.string.page_home,0),
    CONTENT(R.string.page_content,1),
    PROFILE(R.string.page_profile,4),
    PROGRAM(R.string.page_program,5),
    FREE_WORKOUT(R.string.page_free_workout,6),
    TRAINER(R.string.page_trainer,7),
    CONTENT_DETAIL(R.string.page_content,8),
    POPUP_PLAYER(R.string.popup_player),
    POPUP_CAMERA(R.string.popup_camera),

    ACCOUNT(R.string.page_account),
    PAYMENT(R.string.page_payment),
    SETTING(R.string.page_setting),

    PROGRAM_PLAN(R.string.page_planner,2),
    DIET_PLAN(R.string.page_diet_planner),

    REPORT_PROGRAM(R.string.page_program_report),
    REPORT_DIET(R.string.page_diet_report),

    SEARCH_FILTER(R.string.page_search_filter),
    SEARCH(R.string.page_search,3),

    TEST(1000000000,9),
    TEST_POP(1000000002)
}

enum class ParamType(val key:String) {
    PAGES("pages"),
    DETAIL("detail")
}