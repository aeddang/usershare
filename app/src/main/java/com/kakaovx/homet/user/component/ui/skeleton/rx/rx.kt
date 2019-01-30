package com.kakaovx.homet.user.component.ui.skeleton.rx

import com.kakaovx.homet.user.component.network.api.GitHubApi
import io.reactivex.Observable

interface rx{
    fun onSubscribe(){}
    fun <T> setData(data:T){}
    fun <T> injectObservable(o: Observable<T>){}
    fun injectApi(api: GitHubApi){}
}