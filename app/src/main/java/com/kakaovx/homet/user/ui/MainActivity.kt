package com.kakaovx.homet.user.ui

import android.content.Context
import com.kakaovx.homet.lib.page.PageActivity
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.util.Log
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : PageActivity<PageID>(), DivisionTab.Delegate<PageID> {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.activity_main }
    override fun getPageAreaId(): Int { return R.id.area }
    override fun getPageExitMsg(): Int { return R.string.notice_app_exit }
    override fun getHomes():Array<PageID> { return arrayOf(PageID.HOME) }

    @Inject
    lateinit var repository: Repository

    private fun initView(context: Context) {
//        val window = window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = resources.getColor(R.color.indicator)

    }

    override fun onCreated() {
        Log.d(TAG, "onCreated()")
        AndroidInjection.inject(this)

        baseContext?.let { initView(it) }

        repository.setting.isPushEnable()
        PagePresenter.getInstance<PageID>().pageStart(PageID.HOME)
        bottomTab.delegate = this
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
    }

    override fun onSelected(view:DivisionTab<PageID>, id:PageID) {
        Log.d(TAG, "onSelected() = {$id}")
        PagePresenter.getInstance<PageID>().pageChange(id)
    }

    override fun getPageByID(id:PageID): PageFragment {
        Log.d(TAG, "getPageByID() = {$id}")
        bottomTab.setSelect(id)
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun getPopupByID(id:PageID): PageFragment {
        Log.d(TAG, "getPopupByID() = {$id}")
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun getPageIn(isBack:Boolean): Int {
        return if(!isBack) R.anim.slide_in_left else R.anim.slide_in_right
    }
    override fun getPageOut(isBack:Boolean): Int {
        return if(!isBack) R.anim.slide_out_right else R.anim.slide_out_left
    }
    override fun getPopupIn(): Int {
        return R.anim.slide_in_down
    }
    override fun getPopupOut(): Int {
        return  R.anim.slide_out_down
    }
}

