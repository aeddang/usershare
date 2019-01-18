package lib.page

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.kakaovx.homet.R

abstract class PageActivity<T> : AppCompatActivity(), PagePresenter.View<T>, PageFragment.Delegate
{
    open val pagePresenter = PagePresenter(this, PageModel())
    open var currentPage: PageFragment? = null
        protected set

    protected lateinit var pageArea:ViewGroup

    @LayoutRes abstract fun getLayoutResId(): Int
    @IdRes abstract fun getPageAreaId(): Int
    abstract fun init()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        pageArea = findViewById(getPageAreaId())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    open fun getCurentFragment(): PageFragment? {
        return supportFragmentManager.fragments.last() as PageFragment
    }

    open fun getPageAreaSize():Pair<Float,Float> {
        return Pair(pageArea.width.toFloat(),pageArea.height.toFloat())
    }

    abstract fun onBackPressedAction(): Boolean
    override fun onBackPressed() {
        getCurentFragment()?.let{ if(!it.onBack())return }
        if(!pagePresenter.onBack()) return
        if(onBackPressedAction()) return
        super.onBackPressed()
    }

    abstract fun <T> getPageByID(id:T): PageFragment
    final override fun onPageChange(id:T,isBack:Boolean) {
        val willChangePage = getPageByID(id)
        willChangePage.pageID = id
        willChangePage.delegate = this
        willChangePage.pageType = if(isBack) PageFragment.PageType.BACK else PageFragment.PageType.DEFAULT
        supportFragmentManager.beginTransaction().add(getPageAreaId(),willChangePage).commit()
    }

    override fun onCreateAnimation(v:PageFragment) {
        if(v.pageType == PageFragment.PageType.POPUP) return
        currentPage?.let {
            it.pageType = v.pageType
            it.onDestroyAnimation()
        }
        currentPage = v
    }

    abstract fun <T> getPopupByID(id:T): PageFragment
    final override fun onOpenPopup(id:T) {
        val popup = getPopupByID(id)
        popup.pageID = id
        popup.pageType = PageFragment.PageType.POPUP
        supportFragmentManager.beginTransaction().add(getPageAreaId(),popup,id.toString()).commit()
    }

    final override fun onClosePopup(id:T) {
        val popup = supportFragmentManager.findFragmentByTag(id.toString()) as PageFragment
        popup.onDestroyAnimation()
    }
}