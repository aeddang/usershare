package com.kakaovx.homet.user.ui.page

import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.PageID
import kotlinx.android.synthetic.main.page_main.*
import javax.inject.Inject


class PageMain : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var repository: Repository

    override fun getLayoutResId(): Int { return R.layout.page_main }

    override fun onDestroyed() {
        super.onDestroyed()
    }

    override fun onCreated() {
        super.onCreated()
        //AndroidSupportInjection.inject(this)
        //listComponent.injectApi(repository.restApi)

        buttonTestA.setOnClickListener{ PagePresenter.getInstance<PageID>().pageChange( PageID.PROGRAM) }
        buttonTestB.setOnClickListener{ PagePresenter.getInstance<PageID>().pageChange( PageID.PLANNER) }
    }

}