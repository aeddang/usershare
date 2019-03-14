package com.kakaovx.homet.user.ui.player

import android.graphics.Bitmap
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
import com.kakaovx.homet.user.util.Log
import com.kakaovx.posemachine.PoseMachine

class PlayerViewModel(val repo: Repository, private val cv: VxCamera) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    private val _response: MutableLiveData<PageLiveData> = MutableLiveData()
    val response: LiveData<PageLiveData> get() = _response

    private val _content: MutableLiveData<WorkoutData> = MutableLiveData()
    val content: LiveData<WorkoutData> get() = _content

    private val _core: MutableLiveData<VxCoreLiveData> = MutableLiveData()
    val core: LiveData<VxCoreLiveData> get() = _core

    fun intCaptureView() {
        cv.initVxCamera()
        VxCoreObserver.addObserver{ observable, _ ->
            if (observable is VxCoreObserver) {
                val liveData = observable.getData()
                _core.postValue(liveData)
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

    fun poseEstimate(bitmap: Bitmap, callback: PoseMachine.DataProcessCallback) = cv.poseEstimate(bitmap, callback)

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        cv.destroyCamera()
        VxCoreObserver.deleteObservers()
        super.onCleared()
    }
}