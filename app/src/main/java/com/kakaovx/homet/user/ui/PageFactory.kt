package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePosition
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.ui.page.test.PopupDividedGestureTest
import com.kakaovx.homet.user.ui.page.test.PageTest
import com.kakaovx.homet.user.ui.page.content.PageContent
import com.kakaovx.homet.user.ui.page.content.program.PageProgram
import com.kakaovx.homet.user.ui.page.content.search.PageFilter
import com.kakaovx.homet.user.ui.page.content.search.PageSearch
import com.kakaovx.homet.user.ui.page.content.trainer.PageTrainer
import com.kakaovx.homet.user.ui.page.content.workout.PageFreeWorkout
import com.kakaovx.homet.user.ui.page.content.workout.detail.PageContentDetail
import com.kakaovx.homet.user.ui.page.etc.account.PageAccount
import com.kakaovx.homet.user.ui.page.etc.payment.PagePayment
import com.kakaovx.homet.user.ui.page.planner.diet.PageDietPlanner
import com.kakaovx.homet.user.ui.page.planner.program.PagePlanner
import com.kakaovx.homet.user.ui.page.camera.PopupCamera
import com.kakaovx.homet.user.ui.page.etc.account.PopupLogin
import com.kakaovx.homet.user.ui.page.player.PopupPlayer
import com.kakaovx.homet.user.ui.page.profile.PageProfile
import com.kakaovx.homet.user.ui.page.profile.setting.PageSetting
import com.kakaovx.homet.user.ui.page.report.diet.PageDietReport
import com.kakaovx.homet.user.ui.page.report.program.PageProgramReport
import com.kakaovx.homet.user.ui.page.test.PopupGraphTest

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

    /**
     * 홈페이지 등록
     * 등록시 뒤로실행시 옙종료
     */
    val homePages: Array<PageID> = arrayOf( PageID.CONTENT, PageID.PROGRAM_PLAN, PageID.TRAINER, PageID.PROGRAM_REPORT, PageID.PROFILE )

    /**
     * 재사용가능 페이지등록
     * 등록시 viewModel 및 fragment가 재사용 -> 페이지 재구성시 효율적
     */
    val backStackPages: Array<PageID> = arrayOf( PageID.CONTENT, PageID.PROGRAM_PLAN, PageID.TRAINER, PageID.PROGRAM_REPORT, PageID.PROFILE )

    /**
     * 로그인필수 페이지등록
     * 등록시 AccountManager가 판단하여 로그인 팝업 호출
     * 로그인시 원하는 페이지로 자동이동
     * 비로그인시 페이지이동 불가
     */
    private val needLoginPages: Array<PageID> = arrayOf( PageID.PROGRAM_REPORT, PageID.PROGRAM_REPORT )
    fun isNeedLoginPage(id: PageID):Boolean{ return needLoginPages.indexOf(id) != -1 }

    /**
     * 하단메뉴 숨기는 페이지 등록
     * 등록시 하단네비게이션 숨김
     */
    private val tabDisablePages: Array<PageID> = arrayOf(PageID.TEST)
    fun isBottomTabHidden(id: PageID):Boolean{ return tabDisablePages.indexOf(id) != -1 }

    fun getPageByID(id:PageID): PageFragment {
        return when(id) {
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
            PageID.ACCOUNT -> PageAccount()
            PageID.PAYMENT -> PagePayment()
            PageID.DIET_PLAN -> PageDietPlanner()
            PageID.SETTING -> PageSetting()
            PageID.DIET_REPORT -> PageDietReport()
            PageID.PROGRAM_REPORT -> PageProgramReport()

            PageID.POPUP_PLAYER -> PopupPlayer()
            PageID.POPUP_CAMERA -> PopupCamera()
            PageID.POPUP_LOGIN -> PopupLogin()

            PageID.TEST -> PageTest()
            PageID.POPUP_DIVIDED_GESTURE_TEST -> PopupDividedGestureTest()
            PageID.POPUP_GRAPH_TEST -> PopupGraphTest()
        }
    }
}

/**
 * PageID
 * position 값에따라 시작 에니메이션 변경
 * 기존페이지보다 클때 : 오른쪽 -> 왼족
 * 기존페이지보다 작을때 : 왼쪽 -> 오른쪽
 * history back 반대
 */
enum class PageID(val resId: Int, override var position: Int = 9999):PagePosition {
    CONTENT(R.string.page_content, 0),
    PROGRAM(R.string.page_program, 1),
    FREE_WORKOUT(R.string.page_free_workout, 2),
    SEARCH(R.string.page_search, 3),
    SEARCH_FILTER(R.string.page_search_filter, 4),
    TRAINER(R.string.page_trainer, 5),
    CONTENT_DETAIL(R.string.page_content, 6),
    PROGRAM_PLAN(R.string.page_planner, 7),
    DIET_PLAN(R.string.page_diet_planner, 8),
    PROGRAM_REPORT(R.string.page_program_report, 9),
    DIET_REPORT(R.string.page_diet_report, 10),
    PROFILE(R.string.page_profile, 11),
    ACCOUNT(R.string.page_account, 14),
    PAYMENT(R.string.page_payment, 15),
    SETTING(R.string.page_setting, 16),
    POPUP_PLAYER(R.string.popup_player),
    POPUP_CAMERA(R.string.popup_camera),
    POPUP_LOGIN(R.string.popup_login),

    TEST(1000000000),
    POPUP_DIVIDED_GESTURE_TEST(1000000002),
    POPUP_GRAPH_TEST(1000000003)
}

enum class ParamType(val key:String) {
    PAGES("pages"),
    DETAIL("detail")
}