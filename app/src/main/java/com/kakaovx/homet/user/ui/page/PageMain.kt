package com.kakaovx.homet.user.ui.page
import android.util.Log
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.component.repository.Repository
import dagger.android.support.AndroidSupportInjection
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

        buttonTestA.setOnClickListener{ PagePresenter.getInstence<PageID>().pageChange( PageID.TEST) }
        buttonTestB.setOnClickListener{ PagePresenter.getInstence<PageID>().pageChange( PageID.SUB) }
    }

}