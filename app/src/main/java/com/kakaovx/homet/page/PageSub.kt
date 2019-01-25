package com.kakaovx.homet.page
import com.kakaovx.homet.PageID
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectablePageFragment
import lib.page.PagePresenter
import kotlinx.android.synthetic.main.page_sub.*


class PageSub : InjectablePageFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.page_sub }

    override fun inject() {

    }

    override fun onCreated() {
        super.onCreated()
        buttonBack.setOnClickListener{ PagePresenter.getInstence<PageID>()?.onBack() }
    }

    override fun onDestroied() {
        super.onDestroied()
    }

}