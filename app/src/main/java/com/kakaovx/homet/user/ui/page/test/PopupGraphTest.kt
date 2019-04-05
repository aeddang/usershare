package com.kakaovx.homet.user.ui.page.test

import android.graphics.drawable.Drawable
import android.transition.ChangeBounds
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageGestureFragment
import com.kakaovx.homet.user.ui.page.report.program.PageProgramReportViewModel
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_program_report.*
import kotlinx.android.synthetic.main.popup_test.*

class PopupGraphTest : RxPageGestureFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.popup_graph }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }

    /**
     * amount
     * amount 설정
     */
    override fun onSubscribe() {
        val datas = arrayListOf(200.0,200.0,200.0,200.0,200.0)
        val datas2 = arrayListOf(1000.0,1000.0,1000.0,1000.0,1000.0)
        disposables += btnStrat.clicks().subscribe{
            bar.amount = 300.0
            bar2.amount = 900.0
            circle.amount = datas
            polygon.amount = datas2
        }
    }


    /**
     * initSet
     * total, color 설정
     */
    override fun onCreatedView() {

        bar.initSet(1000.0, "#ff00ff")
        bar2.initSet(1000.0,"#ffff00", false)
        polygon.initSet(1000.0, "#000000")
        val colors = arrayOf("#ffff00","#00ff00", "#ff0000", "#0000ff", "#000000")
        circle.initSet(1000.0,colors)

        super.onCreatedView()

    }



}