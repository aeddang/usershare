package com.kakaovx.homet.user.ui

import com.kakaovx.homet.lib.page.PageActivity
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : PageActivity<PageID>(), DivisionTab.Delegate<PageID> {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.activity_main }
    override fun getPageAreaId(): Int { return R.id.area }
    override fun getPageExitMsg(): Int { return R.string.notice_app_exit}

    @Inject
    lateinit var repository: Repository

    override fun onCreated() {
        super.onCreated()
        AndroidInjection.inject(this)

        repository.setting.isPushEnable()
        pagePresenter.pageStart(PageID.MAIN)
        bottomTab.delegate = this
    }
    override fun onSelected(view:DivisionTab<PageID>, id:PageID) {
        pagePresenter.pageChange(id)
    }

    override fun getPageByID(id:PageID): PageFragment {
        bottomTab.setSelect(id)
        return PageFactory.getInstence().getPageByID(id)
    }

    override fun getPopupByID(id:PageID): PageFragment {
        return PageFactory.getInstence().getPageByID(id)
    }
}

