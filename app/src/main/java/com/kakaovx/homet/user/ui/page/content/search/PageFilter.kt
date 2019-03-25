package com.kakaovx.homet.user.ui.page.content.search

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.ParamType
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_home.*
import javax.inject.Inject

class PageFilter : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var viewModel: PageFilterViewModel

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_search)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    @LayoutRes
    override fun getLayoutResId(): Int { return R.layout.page_search }

    override fun setParam(param: Map<String, Any>): PageFragment {
        Log.d(TAG, "setParam() data = [${param[ParamType.DETAIL.key]}]")
        return this
    }

    override fun onSubscribe() {
        disposables += viewModel.getFoo()
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated()")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageFilterViewModel::class.java]

        viewModel.response.observe(this, Observer { message ->
            message?.let {
                Log.d(TAG, "message = [$message]")
            } ?: Log.e(TAG, "message is null")
        })

        context?.let{ initView(it) }
        super.onCreated()
    }
}