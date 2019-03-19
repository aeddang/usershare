package com.kakaovx.homet.user.component.ui.skeleton.view.camera

import android.util.SparseIntArray
import android.view.Surface

class CameraViewModel{

    internal val ORIENTATIONS: SparseIntArray = SparseIntArray()
    internal val MAX_PREVIEW_WIDTH = 1920
    internal val MAX_PREVIEW_HEIGHT = 1080
    var flashSupported: Boolean = false; internal set
    var isFront:Boolean = false
    var isFlash:Boolean = false
    var permissionGranted: Camera.PermissionGranted = Camera.PermissionGranted.UnChecked

    init {
        val modifiOrientation = 180
        ORIENTATIONS.append(Surface.ROTATION_0, 90 + modifiOrientation)
        ORIENTATIONS.append(Surface.ROTATION_90, 0 + modifiOrientation)
        ORIENTATIONS.append(Surface.ROTATION_180, 270 + modifiOrientation)
        ORIENTATIONS.append(Surface.ROTATION_270, 180 + modifiOrientation)
    }

    fun resetPermissionGranted(){
        if(permissionGranted == Camera.PermissionGranted.Denied) permissionGranted = Camera.PermissionGranted.UnChecked
    }

    fun destroy(){

    }

}