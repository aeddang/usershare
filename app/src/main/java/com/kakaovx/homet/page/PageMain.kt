package com.kakaovx.homet.page
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.PageID
import com.kakaovx.homet.R
import com.kakaovx.homet.component.network.RxObservableConverter
import com.kakaovx.homet.component.network.api.GitHubApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lib.page.PageFragment
import lib.page.PagePresenter
import java.util.*
import javax.inject.Inject


class PageMain : PageFragment()
{
    @Inject lateinit var api: GitHubApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.page_main,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        val buttonA = view?.findViewById<Button>(R.id.button_test_A) as Button?
        val buttonB = view?.findViewById<Button>(R.id.button_test_B) as Button?
        buttonA?.setOnClickListener {  PagePresenter.getInstence<PageID>()?.pageChange(PageID.TEST)}

    }


}