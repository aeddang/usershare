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
                      val mr: VxMotionRecognition,
                      private val cv: VxCamera) : ViewModel() {

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
        mr.initMotionRecognition()
        cv.setVideoSize(mr.getInputWidth(), mr.getInputHeight())
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

    fun getVideoSize() = cv.getVideoSize()

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
        cv.destroyCamera()
        mr.destroy()
        super.onCleared()
    }
}