package com.kakaovx.homet.user.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object AppPermissionManager {

    const val PERMISSIONS_REQUEST_CODE = 4221

    private fun checkPermission(ctx: Context, userPermission: String): Boolean {
        var ret = false
        if (ContextCompat.checkSelfPermission(ctx, userPermission) == PackageManager.PERMISSION_GRANTED) {
            ret = true
        }
        return ret
    }

    fun checkPermissions(ctx: Context): Boolean {
        var ret = false
        if (checkPermission(ctx, Manifest.permission.CAMERA)
            && checkPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
            && checkPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ret = true
        }
        return ret
    }

    fun requestPermissions(activity: AppCompatActivity): Boolean {
        val ret = false
        val userPermissions = arrayOf(Manifest.permission.CAMERA,
                                      Manifest.permission.READ_EXTERNAL_STORAGE,
                                      Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(activity, userPermissions, PERMISSIONS_REQUEST_CODE)
        return ret
    }
}