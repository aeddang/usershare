package com.kakaovx.homet.user.component.ui.view.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.media.Image
import android.util.AttributeSet
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import org.tensorflow.demo.env.ImageUtils

@CheckResult
fun ExtractionCamera.motionExtract(): Observable<IntArray> {
    return ExtractionCameraEventObservable(this)
}


open class ExtractionCamera: VXCamera {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private val TAG = javaClass.simpleName
    private var delegate:Delegate? = null
    interface Delegate{
        fun extractionData(camera: VXCamera, data:IntArray ){}
    }
    fun setOnExtractionCameraListener( _delegate:Delegate? ){ delegate = _delegate }
    override fun getNeededPermissions():Array<String>{ return arrayOf( Manifest.permission.CAMERA ) }

    override fun onCreated() {
        super.onCreated()
        this.isFront = true
        cameraRatioType =  CameraRatioType.Custom
        this.captureMode = CaptureMode.Extraction
        this.extractionFps = 5
    }

    override fun onExtract(image: Image) {
        delegate?.extractionData(this, convertYUV420ToARGB8888(image))
    }

    fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
        for (i in planes.indices) {
            val buffer = planes[i].buffer
            if (yuvBytes[i] == null) yuvBytes[i] = ByteArray(buffer.capacity())
            buffer.get(yuvBytes[i])
        }
    }

    private fun convertYUV420ToARGB8888(imgYUV420: Image): IntArray {
        cameraOutputSize?.let {
            val previewWidth = it.width
            val previewHeight = it.height
            val yuvBytes = arrayOfNulls<ByteArray>(3)
            val rgbData = IntArray(previewWidth * previewHeight)
            val planes = imgYUV420.planes
            val yRowStride = planes[0].rowStride
            val uvRowStride = planes[1].rowStride
            val uvPixelStride = planes[1].pixelStride

            fillBytes(planes, yuvBytes)
            ImageUtils.convertYUV420ToARGB8888(yuvBytes[0], yuvBytes[1], yuvBytes[2],
                previewWidth, previewHeight,
                yRowStride, uvRowStride, uvPixelStride,
                rgbData)
            return rgbData
        }
        return IntArray(0)
    }

    private fun convertYUV420ToNV21(imgYUV420: Image): ByteArray {
        val rez: ByteArray

        val buffer0 = imgYUV420.planes[0].buffer
        val buffer2 = imgYUV420.planes[2].buffer
        val buffer0Size = buffer0.remaining()
        val buffer2Size = buffer2.remaining()
        rez = ByteArray(buffer0Size + buffer2Size)

        buffer0.get(rez, 0, buffer0Size)
        buffer2.get(rez, buffer0Size, buffer2Size)

        return rez
    }

    private fun yuvImageToByteArray(image: Image): ByteArray {

        assert(image.format == ImageFormat.YUV_420_888)

        val width = image.width
        val height = image.height
        val planes = image.planes
        val result = ByteArray(width * height * 3 /2)

        var stride = planes[0].rowStride
        if (stride == width) {
            planes[0].buffer.get(result, 0, width)
        } else {
            for (row in 0 until height) {
                planes[0].buffer.position(row * stride)
                planes[0].buffer.get(result, row * width, width)
            }
        }

        stride = planes[1].rowStride
        assert (stride == planes[2].rowStride)
        val rowBytesCb = ByteArray(stride)
        val rowBytesCr = ByteArray(stride)

        for (row in 0 until height / 2) {
            val rowOffset = width * height + width / 2 * row
            planes[1].buffer.position(row * stride)
            planes[1].buffer.get(rowBytesCb, 0, width/2)
            planes[2].buffer.position(row * stride)
            planes[2].buffer.get(rowBytesCr, 0, width/2)

            for (col in 0 until width / 2) {
                result[rowOffset + col * 2] = rowBytesCr[col]
                result[rowOffset + col * 2 + 1] = rowBytesCb[col]
            }
        }
        return result
    }
}

private class ExtractionCameraEventObservable( private val view: ExtractionCamera) : Observable<IntArray>() {
    @SuppressLint("RestrictedApi")
    override fun subscribeActual(observer: Observer<in IntArray>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnExtractionCameraListener(listener)
    }
    private class Listener(
        private val view: ExtractionCamera,
        private val observer: Observer<in IntArray>
    ) : MainThreadDisposable(), ExtractionCamera.Delegate {

        override  fun extractionData(camera: VXCamera, data:IntArray ){
            if (isDisposed) return
            observer.onNext(data)
        }
        override fun onDispose() {
            view.setOnExtractionCameraListener(null)
        }
    }
}

