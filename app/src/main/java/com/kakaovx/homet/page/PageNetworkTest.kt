package com.kakaovx.homet.page
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.App
import com.kakaovx.homet.R
import com.kakaovx.homet.component.network.ApiModule
import com.kakaovx.homet.component.network.DaggerApiComponent
import com.kakaovx.homet.component.network.RxObservableConverter
import com.kakaovx.homet.component.network.api.GitHubApi
import com.kakaovx.homet.component.page.InjectablePageFragment
import java.util.*
import javax.inject.Inject

class PageNetworkTest : InjectablePageFragment()
{
    @Inject lateinit var api: GitHubApi

    override fun inject(fragment: Fragment) {
        fragment.context?.run {
            DaggerApiComponent.builder()
                .appComponent(App.getAppComponent(this)).apiModule(ApiModule())
                .build().inject(this@PageNetworkTest)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.page_sub,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        val button = view?.findViewById(R.id.button) as Button?
        button?.let {
            it.clicks().subscribe(this::getAllUsers)
        }
    }

    private fun handleComplete(data: Objects) {
        print("conplete" + data.toString())
    }

    private fun handleError(err:Throwable) {
        print("error")
    }

    fun getAllUsers(v:Unit){
        val params: MutableMap<String, String> = mutableMapOf()
        api.searchRepositories(params)

        RxObservableConverter.forNetwork(api.searchRepositories(params))
        .subscribe(
            this::handleComplete,
            this::handleError
        )
    }
}