package com.kakaovx.homet.user.ui.player

import android.opengl.GLSurfaceView
import android.view.TextureView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.util.Log

class PlayerViewModel(val repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi
    private val cameraView = repo.camera

    private val _response: MutableLiveData<PageLiveData> = MutableLiveData()
    val response: LiveData<PageLiveData> get() = _response

    private val _content: MutableLiveData<WorkoutData> = MutableLiveData()
    val content: LiveData<WorkoutData> get() = _content

    fun initCaptureView(view: TextureView) = cameraView.initCameraView(view)

    fun initRendererView(view: GLSurfaceView) {}

    fun setExistView(existView: Boolean) {
        cameraView.existView = existView
    }

    fun setChangeCamera(isFront: Boolean = true) = cameraView.changeCameraView(isFront)

    fun isFrontCamera() = cameraView.isFrontCamera()

    fun resumeCamera() = cameraView.resumeCamera()

    fun pauseCamera() = cameraView.pauseCamera()

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        pauseCamera()
        super.onCleared()
    }
}