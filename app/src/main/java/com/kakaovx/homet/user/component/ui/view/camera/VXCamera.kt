package com.kakaovx.homet.user.component.ui.view.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.fragment.app.FragmentActivity
import com.jakewharton.rxbinding3.internal.checkMainThread
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.OverlaySurfaceView
import com.kakaovx.homet.user.component.ui.skeleton.view.AutoFitTextureView
import com.kakaovx.homet.user.component.ui.skeleton.view.camera.Camera
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import kotlinx.android.synthetic.main.ui_camera.view.*

import java.io.File

typealias  VXCameraEventType = String

data class VXCameraEvent( val type: VXCameraEventType, val data:Any? = null) {
    companion object {
        const val CAPTURE_START:VXCameraEventType = "captureStart"
        const val CAPTURE_COMPLETED:VXCameraEventType = "captureCompleted"
        const val EXTRACT_START:VXCameraEventType = "extractStart"
        const val EXTRACT_END:VXCameraEventType = "extractEnd"
        const val ERROR:VXCameraEventType = "error"
    }
}

@CheckResult
fun Camera.init(): Observable<VXCameraEvent> {
    return CameraEventObservable(this)
}

@CheckResult
fun VXCamera.draw(): Observable<Canvas> {
    return DrawCameraEventObservable(this)
}


abstract class VXCamera: Camera, OverlaySurfaceView.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private val TAG = javaClass.simpleName
    private var delegate:Delegate? = null
    interface Delegate{
        fun drawCanvas(camera: VXCamera, canvas: Canvas){}
    }
    fun setOnDrawCameraListener( _delegate:Delegate? ){ delegate = _delegate }
    override fun getTextureView(): AutoFitTextureView { return texture }
    override fun getActivity(): FragmentActivity? {  return PagePresenter.getInstance<Any>().activity as? FragmentActivity }
    override fun getLayoutResId(): Int { return R.layout.ui_camera }

    @CallSuper
    override fun onCreatedView() {
        super.onCreatedView()
        surfaceView.setOnDrawListener( this )
    }

    @CallSuper
    override fun onDestroyedView() {
        super.onDestroyedView()
        surfaceView.setOnDrawListener( null )
    }

    override fun onPreview(){
        surfaceView.postInvalidate()
    }

    override fun drawCanvas(canvas: Canvas) {
        super.drawCanvas(canvas)
        delegate?.drawCanvas(this, canvas)
    }

}


private class DrawCameraEventObservable( private val view: VXCamera) : Observable<Canvas>() {
    @SuppressLint("RestrictedApi")

    override fun subscribeActual(observer: Observer<in Canvas>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnDrawCameraListener(listener)
    }
    private class Listener(
        private val view: VXCamera,
        private val observer: Observer<in Canvas>
    ) : MainThreadDisposable(), VXCamera.Delegate {

        override fun drawCanvas(camera: VXCamera, canvas: Canvas) {
            if (isDisposed) return
            observer.onNext(canvas)
        }
        override fun onDispose() {
            view.setOnDrawCameraListener(null)
        }
    }
}

private class CameraEventObservable( private val view: Camera) : Observable<VXCameraEvent>() {
    @SuppressLint("RestrictedApi")
    override fun subscribeActual(observer: Observer<in VXCameraEvent>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnCameraListener(listener)
    }
    private class Listener( private val view: Camera, private val observer: Observer<in VXCameraEvent>) : MainThreadDisposable(), Camera.Delegate {

        override fun onCaptureStart(camera: Camera){
            if (isDisposed) return
            observer.onNext(VXCameraEvent(VXCameraEvent.CAPTURE_START))
        }
        override fun onCaptureCompleted(camera: Camera, file: File?){
            if (isDisposed) return
            observer.onNext(VXCameraEvent(VXCameraEvent.CAPTURE_COMPLETED, file))
        }
        override fun onExtractStart(camera: Camera){
            if (isDisposed) return
            observer.onNext(VXCameraEvent(VXCameraEvent.EXTRACT_START))
        }
        override fun onExtractEnd(camera: Camera){
            if (isDisposed) return
            observer.onNext(VXCameraEvent(VXCameraEvent.EXTRACT_END))
        }
        override fun onError(camera: Camera, type: Camera.Error, data:Any?){
            if (isDisposed) return
            observer.onNext(VXCameraEvent(VXCameraEvent.ERROR, type))
        }
        override fun onDispose() {
            view.setOnCameraListener(null)
        }
    }
}