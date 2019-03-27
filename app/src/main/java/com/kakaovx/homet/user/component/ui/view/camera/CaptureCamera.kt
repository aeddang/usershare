package com.kakaovx.homet.user.component.ui.view.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.checkMainThread
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.ui_capture_camera.view.*
import java.io.File

import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.camera.Camera
import com.kakaovx.homet.user.component.ui.skeleton.view.util.rotate
import com.kakaovx.homet.user.component.ui.skeleton.view.util.swapHolizental
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

@CheckResult
fun CaptureCamera.capture(): Observable<Bitmap> {
    return CaptureCameraEventObservable(this)
}

private class CaptureCameraEventObservable( private val view: CaptureCamera) : Observable<Bitmap>() {
    @SuppressLint("RestrictedApi")
    override fun subscribeActual(observer: io.reactivex.Observer<in Bitmap>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnCameraListener(listener)
        
    }
    private class Listener( private val view: CaptureCamera, private val observer: Observer<in Bitmap>) : MainThreadDisposable(), Camera.Delegate {

        override fun onCaptureCompleted(camera: Camera, file: File?) {
            if (isDisposed) return
            view.capturedImage?.let { observer.onNext(it) }
        }

        override fun onDispose() {
            view.setOnCameraListener(null)
        }
    }
}


class CaptureCamera: VXCamera {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)


    companion object {
        const val CAPTURE_FILE_NAME = "pic.jpg"
    }
    private val TAG = javaClass.simpleName

    var capturedImage:Bitmap? = null;  private set

    override fun getLayoutResId(): Int { return R.layout.ui_capture_camera }
    override fun getNeededPermissions():Array<String>{
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA )
    }

    override fun onCreatedView() {
        super.onCreatedView()
        cameraRatioType =  CameraRatioType.LargestViewRatio
        getActivity()?.let { file = File(it.getExternalFilesDir(null), CAPTURE_FILE_NAME) }
    }

    override fun onDestroyedView() {
        super.onDestroyedView()
        capturedImage?.recycle()
        capturedImage = null
    }

    override fun onSubscribe() {
        super.onSubscribe()
        btnCapture.clicks().subscribe(this::onTakePicture).apply { disposables?.add(this) }
        btnChangeCamera.clicks().subscribe(this::onToggleCamera).apply { disposables?.add(this) }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onTakePicture(v: Unit) { takePicture() }
    @Suppress("UNUSED_PARAMETER")
    private fun onToggleCamera(v: Unit) { toggleCamera() }

    override fun onCapture() {
        file?.let { f->
            val bitmap = BitmapFactory.decodeFile( f.absolutePath )
            var rotate = getOrientation()
            val rotatedBitmap:Bitmap = bitmap.rotate( rotate )
            if (isSwap() ) {
                this.onCaptureImage(rotatedBitmap.swapHolizental())
            } else {
                this.onCaptureImage(rotatedBitmap)
            }
            Log.d(TAG, "rotation $rotate sensorOrientation $sensorOrientation displayRotation $rotation" )
            super.onCapture()
            // bitmap.recycle()
        }
    }

    open fun onCaptureImage(modifiBitmap:Bitmap){
        capturedImage = modifiBitmap
    }


}