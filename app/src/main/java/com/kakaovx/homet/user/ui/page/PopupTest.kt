package com.kakaovx.homet.user.ui.page

import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.popup_test.*

class PopupTest : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.popup_test }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }


    override fun onCreated() {
        super.onCreated()
        /*
        //api = ViewModelProviders.of(this, apiFactory)[GithubModel::class.java]

        api.getSearchRepositories().subscribe(
            this::handleComplete,
            this::handleError
        ).apply { disposables.add(this) }
        */
    }

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete ($data)")
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }

}