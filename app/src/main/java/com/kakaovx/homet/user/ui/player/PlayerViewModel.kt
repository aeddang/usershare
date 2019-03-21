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
import com.kakaovx.homet.user.component.model.PoseModel
import com.kakaovx.homet.user.component.model.TrainerPoseModel
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.AppDeviceExecutor
import com.kakaovx.homet.user.util.Log
import com.kakaovx.posemachine.PoseMachine
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

class PlayerViewModel(val repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi
    private val cv = repo.camera
    private val mr = repo.mr

    private val _content: MutableLiveData<WorkoutData> = MutableLiveData()
    val content: LiveData<WorkoutData> get() = _content

    private val _core: MutableLiveData<VxCoreLiveData> = MutableLiveData()
    val core: LiveData<VxCoreLiveData> get() = _core

    private val _coreData: BehaviorSubject<VxCoreLiveData> = BehaviorSubject.create()
    val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val message: BehaviorSubject<String> = BehaviorSubject.create()

    private var deviceIO: AppDeviceExecutor? = null
    private var isProcessingImage: Boolean = false

    private val trainerPoseModelList = ArrayList<TrainerPoseModel>()


    fun intCaptureView() {
        deviceIO = AppDeviceExecutor()
        mr.initMotionRecognition()
        cv.setInputVideoSize(mr.getInputWidth(), mr.getInputHeight())
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

    fun getDebugInfo(previewWidth: Int, previewHeight: Int) = mr.getDebugInfo(previewWidth, previewHeight)

    fun drawPose(canvas: Canvas, pose: ArrayList<Array<FloatArray>>) = mr.drawPose(canvas, pose)

    fun processImage(previewSize: Size, frameToCropTransform: Matrix?,
                     rgbFrameBitmap: Bitmap?, croppedBitmap: Bitmap?): Disposable {
        return _coreData.map { coreData ->
                if (coreData.cmd == AppConst.LIVE_DATA_VX_CMD_CAMERA) {
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

    fun startLoader(): Disposable {
        val limitTime = 6
        isLoading.onNext(true)
        message.onNext((limitTime - 1).toString())
        return Observable.interval(1000L, TimeUnit.MILLISECONDS)
            .take(limitTime.toLong())
            .map { count ->
                val time = limitTime - count - 1
                message.onNext(time.toString())
            }
            .doOnComplete {
                isLoading.onNext(false)
            }
            .subscribe()
    }

    fun getTrainerMotionData(motion_id: String): Disposable {
        return restApi.getTrainerMotionData(id = motion_id)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .map { res ->
                res.data
            }
            .map { motion ->
                Observable.fromIterable(motion.key_points)
                    .subscribeOn(Schedulers.io())
                    .map { trainerPoseData ->
                        var i = 0
                        val pose = ArrayList<PoseModel>()
                        while (i < trainerPoseData.pose_vector.size) {
                            val model = PoseModel(trainerPoseData.pose_vector[i],
                                                  trainerPoseData.pose_vector[i + 1],
                                                  trainerPoseData.pose_vector[i + 2])
                            pose.add(model)
                            i += 3
                        }
                        val model = TrainerPoseModel(trainerPoseData.time_stamp, pose.toList())
                        trainerPoseModelList.add(model)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        getDataComplete(trainerPoseModelList.toList())
                    }
                    .subscribe()
            }
            .doOnError { e ->
                Log.e(TAG, "getTrainerMotionData() doOnError[$e]")
            }
            .subscribe({
                Log.d(TAG, "getTrainerMotionData() subscribe")
            }, { e ->
                Log.e(TAG, "getTrainerMotionData() error[$e]")
            })
    }

    private fun getDataComplete(trainerPoseModelList: List<TrainerPoseModel>) {
        Log.d(TAG, "getDataComplete() start")
//        for ((count, trainerPose) in trainerPoseModelList.withIndex()) {
//            Log.d(TAG, "[$count]getTimeStamp = [${trainerPose.timestamp}]")
//            for ((count2, pose) in trainerPose.poseData.withIndex()) {
//                Log.d(TAG, "[$count]getPositions = [$count2][${pose.positionX}][${pose.positionY}][${pose.similarity}]")
//            }
//        }
//        Log.d(TAG, "getDataComplete() end")
    }

    @Synchronized
    private fun poseEstimate(data: IntArray, previewSize: Size,
                             frameToCropTransform: Matrix?,
                             rgbFrameBitmap: Bitmap?, croppedBitmap: Bitmap?) {

        rgbFrameBitmap ?: return
        croppedBitmap ?: return
        frameToCropTransform ?: return

        isProcessingImage = true

        rgbFrameBitmap.setPixels(data, 0, previewSize.width, 0, 0, previewSize.width, previewSize.height)
        val canvas = Canvas(croppedBitmap)
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null)

        val pose = mr.poseEstimate(croppedBitmap, PoseMachine.DataProcessCallback {
            //            Log.d(TAG, "onBitmapPrepared()")
        })
//        pose?.let { Log.d(TAG, "Detect Skeletons: [${it.size}]") }

        val liveData = VxCoreLiveData()
        liveData.cmd = AppConst.LIVE_DATA_VX_CMD_CAMERA
        liveData.cameraCmd = AppConst.HOMET_CAMERA_CMD_REQUEST_DRAW
        liveData.poseData = pose
        _core.postValue(liveData)

        isProcessingImage = false
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        cv.destroyCamera()
        mr.destroy()
        VxCoreObserver.deleteObservers()
        deviceIO?.shutdown()
        super.onCleared()
    }
}