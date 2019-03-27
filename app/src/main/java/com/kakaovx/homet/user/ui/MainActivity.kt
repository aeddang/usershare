package com.kakaovx.homet.user.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.lib.page.PageActivity
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.util.Log
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : PageActivity<PageID>(), DivisionTab.Delegate<PageID> {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreatedView() {
        Log.d(TAG, "onCreatedView()")
        AndroidInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[MainActivityViewModel::class.java]
        viewModel.onCreateView()
        viewModel.response?.observe(this, Observer { message ->
            message?.let {
                Log.d(TAG, "message = [$message]")
            } ?: Log.e(TAG, "message is null")
        })

        viewModel.pushEnable()
        PagePresenter.getInstance<PageID>().pageStart(PageID.CONTENT)
        bottomTab.delegate = this
    }

    override fun onDestroyedView() {
        Log.d(TAG, "onDestroyedView()")
        viewModel.onDestroyView()
    }

    override fun onSelected(view:DivisionTab<PageID>, id:PageID) {
        Log.d(TAG, "onSelected() = {$id}")
        PagePresenter.getInstance<PageID>().pageChange(id)
    }

    override fun getPageByID(id:PageID): PageFragment {
        Log.d(TAG, "getPageByID() = {$id}")
        bottomTab.setSelect(id)
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun getPopupByID(id:PageID): PageFragment {
        Log.d(TAG, "getPopupByID() = {$id}")
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun onWillChangePageFragment(id:PageID, param:Map<String, Any>?, isPopup:Boolean){
        if( PageFactory.getInstance().isBottomTabHidden(id) ) bottomTab.hideTab() else bottomTab.viewTab()
    }






    override fun getLayoutResId(): Int { return R.layout.activity_main }
    override fun getPageAreaId(): Int { return R.id.area }
    override fun getPageExitMsg(): Int { return R.string.notice_app_exit }
    override fun getHomes():Array<PageID> { return PageFactory.getInstance().homePages }
    override fun getBackStacks():Array<PageID> { return PageFactory.getInstance().backStackPages }
    override fun getPageIn(isBack:Boolean): Int { return if(isBack) R.anim.slide_in_left else R.anim.slide_in_right }
    override fun getPageOut(isBack:Boolean): Int { return if(isBack) R.anim.slide_out_right else R.anim.slide_out_left }
    override fun getPopupIn(): Int { return R.anim.slide_in_down }
    override fun getPopupOut(): Int { return  R.anim.slide_out_down }
}

