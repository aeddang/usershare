package com.kakaovx.homet.user.ui.page.report.program

import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.ParamType
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_program_report.*
import javax.inject.Inject

class PageProgramReport : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var viewModel: PageProgramReportViewModel



    @LayoutRes
    override fun getLayoutResId(): Int = R.layout.page_program_report

    override fun setParam(param: Map<String, Any>): PageFragment {
        Log.d(TAG, "setParam() data = [${param[ParamType.DETAIL.key]}]")
        return this
    }

    override fun onSubscribe() {

        val datas = arrayListOf(200.0,200.0,200.0,200.0,200.0)
        disposables += viewModel.getFoo()
        disposables += btnStrat.clicks().subscribe{
            bar.amount = 300.0
            bar2.amount = 900.0
            circle.amount = datas
        }
    }

    override fun onCreatedView() {
        Log.d(TAG, "onCreatedView()")
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageProgramReportViewModel::class.java]
        viewModel.onCreateView()
        viewModel.response?.observe(this, Observer { message ->
            message?.let {
                Log.d(TAG, "message = [$message]")
            } ?: Log.e(TAG, "message is null")
        })
        bar.initSet(1000.0, "#ff00ff")
        bar2.initSet(1000.0,"#ffff00", false)

        val colors = arrayOf("#ffff00","#00ff00", "#ff0000", "#0000ff", "#000000")
        circle.initSet(1000.0,colors)
        super.onCreatedView()

    }

    override fun onDestroyedView() {
        Log.d(TAG, "onDestroyedView()")
        super.onDestroyedView()
        viewModel.onDestroyView()
    }
}