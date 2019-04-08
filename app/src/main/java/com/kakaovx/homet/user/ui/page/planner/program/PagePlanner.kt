package com.kakaovx.homet.user.ui.page.planner.program

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.page_planner.*
import javax.inject.Inject

class PagePlanner : RxPageFragment() {

    private val logTag = "page_planner_log"

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var viewModel: PagePlannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    @LayoutRes
    override fun getLayoutResId(): Int {
        return R.layout.page_planner
    }

    override fun onCreatedView() {
        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PagePlannerViewModel::class.java]
        viewModel.onCreateView()
        initViewModel()
        initView()
        viewInflater()
        super.onCreatedView()
    }

    override fun onDestroyedView() {
        super.onDestroyedView()
        viewModel.onDestroyView()
    }

    override fun setParam(param: Map<String, Any>): PageFragment {
        Log.d(logTag, "setParam() data = [${param[ParamType.DETAIL.key]}]")
        return this
    }

    override fun onSubscribe() {
        //disposables += viewModel.getFoo()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.page_planner_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                Log.d(logTag, "menu select")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        val myActivity = activity as MainActivity
        myActivity.setSupportActionBar(planner_toolbar)
        myActivity.supportActionBar?.apply {
            title = getString(R.string.page_planner)
            setDisplayShowTitleEnabled(true)
        }
    }

    private fun initViewModel() {
        viewModel.response?.observe(this, Observer { message ->
            message?.let {
                Log.d(logTag, "message = [$message]")
            } ?: Log.e(logTag, "message is null")
        })
    }

    private fun viewInflater() {
        val programView = LayoutInflater.from(context).inflate(R.layout.page_planner_program, program_layout, true)
        val favoriteView = LayoutInflater.from(context).inflate(R.layout.page_planner_favorite, favorites_layout, true)
        val dietView = LayoutInflater.from(context).inflate(R.layout.page_planner_diet, diet_layout, true)
    }
}