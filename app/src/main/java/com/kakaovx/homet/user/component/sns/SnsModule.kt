package com.kakaovx.homet.user.component.sns

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable


@CheckResult
fun SnsModule.statusChanged(): Observable<SnsStatus> {
    return StatusChangedObservable(this)
}

fun SnsModule.profileUpdated(): Observable<SnsProfile> {
    val observable = ProfileUpdatedObservable(this)
    this.requestProfile()
    return observable
}

fun SnsModule.error(): Observable<SnsError> {
    return ErrorObservable(this)
}

abstract class SnsModule(val type:SnsType):Sns{
    private val TAG = javaClass.simpleName

    var status:SnsStatus = SnsStatus.Logout; private set
    protected var delegate:Delegate? = null
    protected var delegateError:DelegateError? = null
    protected var delegateProfile:DelegateProfile? = null
    var userProfile:SnsProfile? = null; private set

    interface Delegate{
        fun statusChanged(sns: SnsModule, status:SnsStatus){}
    }
    interface DelegateError{
        fun onError(sns: SnsModule, error:SnsError){}
    }
    interface DelegateProfile{
        fun profileUpdated(sns: SnsModule, profile:SnsProfile){}
        fun onError(sns: SnsModule, error:SnsError){}
    }
    fun setOnStatusChangedListener( _delegate: SnsModule.Delegate? ){ delegate = _delegate }
    fun setOnErrorListener( _delegate: SnsModule.DelegateError? ){ delegateError = _delegate }
    fun setOnProfileUpdatedListener( _delegate: SnsModule.DelegateProfile? ){ delegateProfile = _delegate }

    @CallSuper
    override fun destroy() {
        userProfile = null
    }


    protected fun onStatusChanged(st:SnsStatus){
        if(status == SnsStatus.Logout) userProfile = null
        if( st != status ) delegate?.statusChanged(this, st)
        status = st
        Log.d(TAG, "status $status")

    }
    protected fun onStatusError(error:SnsError){
        delegateError?.onError(this, error)
    }

    protected fun onProfileError(error:SnsError){
        delegateProfile?.onError(this, error)
    }

    protected fun onProfileUpdated(profile:SnsProfile){
        userProfile = profile
        delegateProfile?.profileUpdated(this, profile)
    }
}

private class StatusChangedObservable( private val module: SnsModule) : Observable<SnsStatus>() {
    @SuppressLint("RestrictedApi")

    override fun subscribeActual(observer: Observer<in SnsStatus>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener( module, observer)
        observer.onSubscribe(listener)
        module.setOnStatusChangedListener(listener)
    }
    private class Listener(
        private val module: SnsModule,
        private val observer: Observer<in SnsStatus>
    ) : MainThreadDisposable(), SnsModule.Delegate {

        override fun statusChanged(sns: SnsModule, status:SnsStatus) {
            if (isDisposed) return
            observer.onNext(status)
        }
        override fun onDispose() {
            module.setOnStatusChangedListener(null)
        }
    }
}

private class ProfileUpdatedObservable( private val module: SnsModule) : Observable<SnsProfile>() {
    @SuppressLint("RestrictedApi")
    override fun subscribeActual(observer: Observer<in SnsProfile>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener( module, observer)
        observer.onSubscribe(listener)
        module.setOnProfileUpdatedListener(listener)
    }
    private class Listener(
        private val module: SnsModule,
        private val observer: Observer<in SnsProfile>
    ) : MainThreadDisposable(), SnsModule.DelegateProfile {

        override fun profileUpdated(sns: SnsModule, profile:SnsProfile) {
            if (isDisposed) return
            observer.onNext(profile)
            observer.onComplete()
        }

        override fun onError(sns: SnsModule, error:SnsError) {
            if (isDisposed) return
            observer.onError(Throwable(error.name))
        }

        override fun onDispose() {
            module.setOnStatusChangedListener(null)
        }
    }
}


private class ErrorObservable( private val module: SnsModule) : Observable<SnsError>() {
    @SuppressLint("RestrictedApi")
    override fun subscribeActual(observer: Observer<in SnsError>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener( module, observer)
        observer.onSubscribe(listener)
        module.setOnErrorListener(listener)
    }
    private class Listener(
        private val module: SnsModule,
        private val observer: Observer<in SnsError>
    ) : MainThreadDisposable(), SnsModule.DelegateError {

        override fun onError(sns: SnsModule, error:SnsError) {
            if (isDisposed) return
            observer.onNext(error)
        }
        override fun onDispose() {
            module.setOnStatusChangedListener(null)
        }
    }
}