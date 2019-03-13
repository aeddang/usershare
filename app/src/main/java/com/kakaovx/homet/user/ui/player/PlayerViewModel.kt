package com.kakaovx.homet.user.ui.player

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.vxcore.VxCamera
import com.kakaovx.homet.user.component.vxcore.VxMotionRecognition
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class PlayerViewModel(val repo: Repository,
                      val motionRecognition: VxMotionRecognition,
                      private val cameraView: VxCamera) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    private val _response: MutableLiveData<PageLiveData> = MutableLiveData()
    val response: LiveData<PageLiveData> get() = _response

    private val _content: MutableLiveData<WorkoutData> = MutableLiveData()
    val content: LiveData<WorkoutData> get() = _content

    private val _core: MutableLiveData<VxCoreLiveData> = MutableLiveData()
    val core: LiveData<VxCoreLiveData> get() = _core

    fun intCaptureView() {
        cameraView.initVxCamera()
        motionRecognition.initMotionRecognition()
    }

    fun initRendererView(view: GLSurfaceView) {}

    fun setExistView(existView: Boolean) {
        cameraView.existView = existView
    }

    fun setSurfaceTextureData(texture: SurfaceTexture?) {
        cameraView.surfaceTexture = texture
    }

    fun setChangeCamera(isFront: Boolean = true) = cameraView.changeCameraView(isFront)

    fun setPreviewVideoSize(preview: Size) {
        cameraView.videoWidth = preview.width
        cameraView.videoHeight = preview.height
    }

    fun isFrontCamera() = cameraView.isFrontCamera()

    fun resumeCamera() = cameraView.resumeCamera()

    fun pauseCamera() = cameraView.pauseCamera()

    fun getCameraId() = cameraView.cameraId

    fun getVideoSize() = cameraView.getVideoSize()

    private fun sendCommand(cmd: Int, width: Int = 0, height: Int = 0) {
        val liveData = VxCoreLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_CAMERA
        liveData.cameraCmd = cmd
        liveData.width = width
        liveData.height = height
        _core.value = liveData
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        cameraView.destroyCamera()
        motionRecognition.destroy()
        super.onCleared()
    }
}