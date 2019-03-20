package com.kakaovx.homet.user.component.ui.skeleton.view.camera

import android.util.SparseIntArray
import android.view.Surface

class CameraViewModel{

    internal val ORIENTATIONS: SparseIntArray = SparseIntArray()
    var flashSupported: Boolean = false; internal set
    var isFront:Boolean = false
    var isFlash:Boolean = true
    var permissionGranted: Camera.PermissionGranted = Camera.PermissionGranted.UnChecked

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90 )
        ORIENTATIONS.append(Surface.ROTATION_90, 0 )
        ORIENTATIONS.append(Surface.ROTATION_180, 270 )
        ORIENTATIONS.append(Surface.ROTATION_270, 180 )
    }

    fun resetPermissionGranted(){
        if(permissionGranted == Camera.PermissionGranted.Denied) permissionGranted = Camera.PermissionGranted.UnChecked
    }

    fun destroy(){

    }

}