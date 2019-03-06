package com.kakaovx.homet.user.component.ui.skeleton.rx

import com.kakaovx.homet.user.component.network.api.RestfulApi
import io.reactivex.Observable

interface Rx {
    fun onSubscribe(){}
    fun <T> setData(data:T){}
    fun <T> injectObservable(o: Observable<T>){}
    fun injectApi(api: RestfulApi){}
}