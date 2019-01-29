package com.kakaovx.homet.user.ui.page

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.component.network.viewmodel.GithubModel
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageDividedGestureFragment
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.popup_test.*

class PopupTest : ApiPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.popup_test }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    private lateinit var viewModel: GithubModel

    override fun onCreated() {
        super.onCreated()
        viewModel = ViewModelProviders.of(this, apiModelFactory)[GithubModel::class.java]

        viewModel.getSearchRepositories().subscribe(
            this::handleComplete,
            this::handleError
        ).apply { disposables.add(this) }
    }

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete ($data)")
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }

}