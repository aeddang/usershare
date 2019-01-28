package com.kakaovx.homet.user.ui

import android.view.View
import android.widget.Toast
import com.kakaovx.homet.lib.module.Gesture
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.lib.page.PageNavigationActivity
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.util.Log
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
        return PageFactory.getInstence().getPageByID(id as PageID)
    }

    override fun <T> getPopupByID(id:T): PageFragment {
        resetBackPressedAction()
        return PageFactory.getInstence().getPageByID(id as PageID)
    }
}

