package com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel
import androidx.annotation.CallSuper
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.util.Log
import io.reactivex.disposables.Disposable

abstract class ApiModel<T> (val restApi: RestfulApi)  {

    val TAG = javaClass.simpleName
    var responseData: T?  = null; private set
    var status:Status = Status.None;  private set

    @CallSuper
    protected open fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
        status = Status.Error
    }

    @CallSuper
    protected open fun handleComplete(responseData:T) {
        this.responseData = responseData
        status = Status.Complete
    }
    fun load () : Disposable { return load( null ) }
    abstract fun load (param:Map<String, Any>?) : Disposable
    abstract fun cancel ()
}

