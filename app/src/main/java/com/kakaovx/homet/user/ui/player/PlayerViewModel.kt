package com.kakaovx.homet.user.ui.player

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.vxcore.VxCamera
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.AppDeviceExecutor
import com.kakaovx.homet.user.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class PlayerViewModel(val repo: Repository, private val cv: VxCamera) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    private val _response: MutableLiveData<PageLiveData> = MutableLiveData()
    val response: LiveData<PageLiveData> get() = _response

    private val _content: MutableLiveData<WorkoutData> = MutableLiveData()
    val content: LiveData<WorkoutData> get() = _content

    private val _core: MutableLiveData<VxCoreLiveData> = MutableLiveData()
    val core: LiveData<VxCoreLiveData> get() = _core

    private val _coreData: BehaviorSubject<VxCoreLiveData> = BehaviorSubject.create()

    private var deviceIO: AppDeviceExecutor? = null
    private var isProcessingImage: Boolean = false

    fun intCaptureView() {
        deviceIO = AppDeviceExecutor()
        cv.initVxCamera()
        VxCoreObserver.addObserver{ observable, _ ->
            if (observable is VxCoreObserver) {
                observable.getData()?.let {
                    if (isProcessingImage.not())
                        _coreData.onNext(it)
                }
            }
        }
    }

    fun initRendererView(view: GLSurfaceView) {}

    fun setExistView(existView: Boolean) {
        cv.existView = existView
    }

    fun setSurfaceTextureData(texture: SurfaceTexture?) {
        cv.surfaceTexture = texture
    }

    fun setChangeCamera(isFront: Boolean = true) = cv.changeCameraView(isFront)

    fun setPreviewVideoSize(preview: Size) {
        cv.videoWidth = preview.width
        cv.videoHeight = preview.height
    }

    fun isFrontCamera() = cv.isFrontCamera()

    fun resumeCamera() = cv.resumeCamera()

    fun pauseCamera() = cv.pauseCamera()

    fun getCameraId() = cv.cameraId

    fun getInputVideoSize() = cv.getInputVideoSize()

    fun getDebugInfo(previewWidth: Int, previewHeight: Int) = cv.getDebugInfo(previewWidth, previewHeight)

    fun drawPose(canvas: Canvas, pose: ArrayList<Array<FloatArray>>) = cv.drawPose(canvas, pose)

    fun processImage(previewSize: Size, frameToCropTransform: Matrix?,
                     rgbFrameBitmap: Bitmap?, croppedBitmap: Bitmap?): Disposable {
        return _coreData.map { coreData ->
                if (coreData.cmd == AppConst.LIVE_DATA_CMD_CAMERA) {
                    when (coreData.cameraCmd) {
                        AppConst.HOMET_CAMERA_CMD_ON_IMAGE_AVAILABLE -> {
                            coreData.data?.let {
                                deviceIO?.execute {
                                    poseEstimate(it, previewSize, frameToCropTransform, rgbFrameBitmap, croppedBitmap)
                                }
                            }
                        }
                        else -> {
                            Log.e(TAG, "wrong camera cmd")
                        }
                    }
                } else {
                    Log.e(TAG, "wrong cmd")
                }
            }
            .subscribe()
    }

//    fun poseEstimate(bitmap: Bitmap, callback: PoseMachine.DataProcessCallback) = cv.poseEstimate(bitmap, callback)

    @Synchronized
    private fun poseEstimate(data: IntArray, previewSize: Size,
                             frameToCropTransform: Matrix?,
                             rgbFrameBitmap: Bitmap?, croppedBitmap: Bitmap?) {
//        Log.d(TAG, "poseEstimate() Thread id = [${Thread.currentThread().id}]")

        rgbFrameBitmap ?: return
        croppedBitmap ?: return
        frameToCropTransform ?: return

        isProcessingImage = true

        rgbFrameBitmap.setPixels(data, 0, previewSize.width, 0, 0, previewSize.width, previewSize.height)
        val canvas = Canvas(croppedBitmap)
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null)

        val pose = cv.poseEstimate(croppedBitmap)
//        pose?.let { Log.d(TAG, "Detect Skeletons: [${it.size}]") }

        val liveData = VxCoreLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_CAMERA
        liveData.cameraCmd = AppConst.HOMET_CAMERA_CMD_REQUEST_DRAW
        liveData.poseData = pose
        _core.postValue(liveData)

        isProcessingImage = false
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        cv.destroyCamera()
        VxCoreObserver.deleteObservers()
        deviceIO?.shutdown()
        super.onCleared()
    }
}