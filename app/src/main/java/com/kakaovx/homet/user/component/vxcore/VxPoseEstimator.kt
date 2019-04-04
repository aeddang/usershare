package com.kakaovx.homet.user.component.vxcore

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import com.kakaovx.homet.user.component.model.PoseModel
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.Log
import com.kakaovx.posemachine.MaceWrapper
import com.kakaovx.posemachine.PoseMachine
import com.kakaovx.posemachine.Keypoints
import java.util.ArrayList

class VxPoseEstimator(val app: Application, val context: Context) {

    val TAG = javaClass.simpleName

    private var poseMachine: PoseMachine? = null

    fun initPoseEstimator() {
        Log.i(TAG, "initPoseEstimator()")
        if (poseMachine == null) {
            if (Build.MODEL == "SHIELD Android TV" || Build.DEVICE == "darcy") {
                poseMachine = PoseMachine(app, MaceWrapper.MODEL_TYPE.PersonLab, MaceWrapper.RUNTIME_TYPE.CPU)
            } else {
                poseMachine = PoseMachine(app, MaceWrapper.MODEL_TYPE.PersonLab, MaceWrapper.RUNTIME_TYPE.GPU)
            }
            poseMachine?.apply {
                Log.d(TAG, "get inputWidth = [$inputWidth]")
                Log.d(TAG, "get inputHeight = [$inputHeight]")
            }
            return
        }
        Log.e(TAG, "Pose Machine already init()")
    }

    fun destroy() {
        Log.i(TAG, "destroy()")
        poseMachine?.apply {
            this.destroy()
        }
        poseMachine = null
    }

    fun getInputWidth() = poseMachine?.inputWidth ?: AppFeature.APP_FEATURE_VIDEO_WIDTH

    fun getInputHeight() = poseMachine?.inputHeight ?: AppFeature.APP_FEATURE_VIDEO_HEIGHT

    fun getDebugInfo(previewWidth: Int, previewHeight: Int) = poseMachine?.getDebugInfo(previewWidth, previewHeight)

    fun drawPose(canvas: Canvas, pose: ArrayList<Array<FloatArray>>) = poseMachine?.drawPose(canvas, pose, true)

    fun poseEstimate(bitmap: Bitmap, callback: PoseMachine.DataProcessCallback): ArrayList<Array<FloatArray>>? {
        //val lStartTime = System.currentTimeMillis()
        val data = poseMachine?.poseEstimate(bitmap, callback)
        //val elapseTime = System.currentTimeMillis() - lStartTime
        //Log.d(TAG, "poseEstimate() elapseTime = [$elapseTime]ms")
        return data
    }

    fun simpleCalculateSimilarity(pose: ArrayList<Array<FloatArray>>, trainerPose: List<PoseModel>): Array<IntArray> {
        val positionThreshold = 30
        val ret: Array<IntArray> = Array(Keypoints.NUM_KEYPOINTS) {IntArray(3)}

//        var trainerPoseDataStr = "Get TrainerPoseData\n"
//        var i = 0
//        for (data in trainerPose) {
//            trainerPoseDataStr += "index[${i++}]X[${data.positionX}]Y[${data.positionY}]\n"
//        }
//        Log.d(TAG, "simpleCalculateSimilarity() $trainerPoseDataStr")
//
//        var poseDataStr = "Get PoseData\n"
//        i = 0
//        for (floatArray in pose) {
//            for (j in floatArray.indices) {
//                val data = floatArray[j]
//                poseDataStr += "index i[$i]index j[$j]X[${data[0]}]Y[${data[1]}]\n"
//            }
//            i++
//        }
//        Log.d(TAG, "simpleCalculateSimilarity() $poseDataStr")

//        var poseScore = "Get PoseScore\n"
        val myPose = pose[0]
        for (jointPosition in ret.indices) {
            val trainerX = trainerPose[jointPosition].positionX
            val poseX = myPose[jointPosition][0]
            Log.d(TAG, "jointPosition[$jointPosition]trainerX[$trainerX]poseX[$poseX]result[${trainerX - poseX}]")
            if (poseX > trainerX - positionThreshold && poseX < trainerX + positionThreshold) {
                ret[jointPosition][0] = 1
            } else {
                ret[jointPosition][0] = 0
            }

            val trainerY = trainerPose[jointPosition].positionY
            val poseY = myPose[jointPosition][1]
            Log.d(TAG, "jointPosition[$jointPosition]trainerY[$trainerY]poseY[$poseY]result[${trainerY - poseY}]")
            if (poseY > trainerY - positionThreshold && poseY < trainerY + positionThreshold) {
                ret[jointPosition][1] = 1
            } else {
                ret[jointPosition][1] = 0
            }
//            poseScore += "index [$jointPosition]X[${ret[jointPosition][0]}]Y[${ret[jointPosition][1]}]\n"
        }
//        Log.d(TAG, "simpleCalculateSimilarity() $poseScore")

        return ret
    }
}