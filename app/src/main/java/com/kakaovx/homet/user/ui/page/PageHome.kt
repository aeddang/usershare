package com.kakaovx.homet.user.ui.page

import android.content.Context
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.page_main.*
import javax.inject.Inject


class PageHome : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var repository: Repository

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_home)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    override fun getLayoutResId(): Int { return R.layout.page_main }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        super.onDestroyed()
    }

    override fun onCreated() {
        super.onCreated()

        Log.d(TAG, "onCreated()")
        // AndroidSupportInjection.inject(this)
        // listComponent.injectApi(repository.restApi)

        context?.let {
            initView(it)
        }

        buttonTestA.setOnClickListener{ PagePresenter.getInstance<PageID>().pageChange( PageID.PROGRAM) }
        buttonTestB.setOnClickListener{ PagePresenter.getInstance<PageID>().pageChange( PageID.PLANNER) }
    }

}