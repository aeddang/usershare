package lib.page

import android.os.Bundle
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

    private lateinit var pageArea:ViewGroup

    @LayoutRes abstract fun getLayoutResId(): Int
    @IdRes abstract fun getPageAreaId(): Int
    abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        pageArea = findViewById(getPageAreaId())
        init()
    }

    open fun getCurentFragment(): PageFragment?
    {
        supportFragmentManager.fragments?.let {
            val cf = it.last() as PageFragment
            return cf
        }
        return null
    }

    open fun getPageAreaSize():Pair<Float,Float>
    {
        pageArea?.let { return Pair(it.width.toFloat(),it.height.toFloat())}
        return Pair(0f,0f)
    }

    override fun onBackPressed()
    {
        getCurentFragment()?.let{ if(!it.onBack())return }
        if(!pagePresenter.onBack()) return
        super.onBackPressed()
    }

    open protected fun <T> getPageByID(id:T): PageFragment? {return null}
    final override fun onPageChange(id:T,isBack:Boolean)
    {
        val willChangePage = getPageByID(id)
        willChangePage?.let {
            it.pageID = id
            it.delegate = this
            it.pageType = if(isBack) PageFragment.PageType.BACK else PageFragment.PageType.DEFAULT
            supportFragmentManager.beginTransaction().add(getPageAreaId(),it).commit()
        }
    }

    override fun onCreateAnimation(v:PageFragment)
    {
        if(v.pageType == PageFragment.PageType.POPUP) return
        currentPage?.let {
            it.pageType = v.pageType
            it.onDestroyAnimation()
        }
        currentPage = v
    }

    open protected fun <T> getPopupByID(id:T): PageFragment? {return null}
    final override fun onOpenPopup(id:T)
    {
        val popup = getPopupByID(id)
        popup?.pageID = id
        popup?.let {
            it.pageType = PageFragment.PageType.POPUP
            supportFragmentManager.beginTransaction().add(getPageAreaId(),it,id.toString()).commit()
        }
    }

    final override fun onClosePopup(id:T)
    {
        val popup = supportFragmentManager.findFragmentByTag(id.toString()) as PageFragment
        popup?.let { it.onDestroyAnimation()}
    }
}