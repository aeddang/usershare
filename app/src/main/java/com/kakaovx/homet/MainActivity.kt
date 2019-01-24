package com.kakaovx.homet

import android.view.View
import android.widget.Toast
import com.kakaovx.homet.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.page.PageMain
import com.kakaovx.homet.page.PageNetworkTest
import com.kakaovx.homet.page.PopupTest
import com.kakaovx.homet.util.Log
import lib.page.PageFragment
import lib.page.PageGestureView
import lib.page.PageNavigationActivity
import lib.module.Gesture
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : PageNavigationActivity<PageID>(), DivisionTab.Delegate {

    private val TAG = javaClass.simpleName

    private var exitCount = 0

    override fun getLayoutResId(): Int { return R.layout.activity_main }
    override fun getPageAreaId(): Int { return R.id.area }
    override fun getNavigationView(): PageGestureView { return navigation }
    override fun getContentsView(): View { return contents }
    override fun getNavigationViewBgView(): View { return navigationBg }
    override fun getCloseType(): Gesture.Type { return Gesture.Type.PAN_RIGHT }

    override fun onCreated() {
        super.onCreated()
        pagePresenter.pageStart(PageID.MAIN)
        bottomTab.delegate = this
    }
    private fun resetBackPressedAction() {
        exitCount = 0
    }
    override fun onBackPressedAction(): Boolean {
        if(exitCount == 1) return false
        exitCount ++
        Toast.makeText(this,R.string.notice_app_exit,Toast.LENGTH_LONG).show()
        return true
    }

    override fun onSelected(view: DivisionTab, idx:Int) {
        Log.i(TAG, "onSelected" + idx.toString())
    }

    override fun <T> getPageByID(id:T): PageFragment {
        resetBackPressedAction()
        return when(id) {
            PageID.MAIN -> { PageMain() }
            PageID.SUB -> { PageNetworkTest() }
            else -> { PageMain() }
        }
    }

    override fun <T> getPopupByID(id:T): PageFragment {
        resetBackPressedAction()
        return when(id) {
            PageID.POPUP_TEST -> { PopupTest() }
            else -> { PageMain() }
        }
    }
}

enum class PageID {
    MAIN,SUB,TEST,
    POPUP_TEST
}
