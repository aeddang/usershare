package com.kakaovx.homet.user.component.ui.view.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.checkMainThread
import com.kakaovx.homet.user.component.ui.skeleton.view.AnimatedDrawView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

@CheckResult
fun VXGraph.draw(): Observable<ArrayList<Pair<Double,Point>>> {
    return DrawGraphEventObservable(this)
}


abstract class VXGraph@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  AnimatedDrawView(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName

    protected var delegate:Delegate? = null
    interface Delegate{
        fun drawGraph(graph: VXGraph, datas:ArrayList<Pair<Double,Point>>){}
    }
    fun setOnDrawGraphListener( _delegate:Delegate? ){ delegate = _delegate }
}


private class DrawGraphEventObservable( private val view: VXGraph) : Observable<ArrayList<Pair<Double,Point>>>() {
    @SuppressLint("RestrictedApi")

    override fun subscribeActual(observer: Observer<in ArrayList<Pair<Double,Point>>>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnDrawGraphListener(listener)
    }
    private class Listener(
        private val view: VXGraph,
        private val observer: Observer<in ArrayList<Pair<Double,Point>>>
    ) : MainThreadDisposable(), VXGraph.Delegate {

        override fun drawGraph(graph: VXGraph, datas:ArrayList<Pair<Double,Point>>) {
            if (isDisposed) return
            observer.onNext(datas)
        }
        override fun onDispose() {
            view.setOnDrawGraphListener(null)
        }
    }
}