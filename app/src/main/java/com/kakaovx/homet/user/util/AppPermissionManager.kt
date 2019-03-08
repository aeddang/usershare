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
        if (checkPermission(ctx, Manifest.permission.CAMERA)) {
            ret = true
        }
        return ret
    }

    fun requestPermissions(activity: AppCompatActivity): Boolean {
        val ret = false
        val userPermissions = arrayOf(
            Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(activity, userPermissions, PERMISSIONS_REQUEST_CODE)
        return ret
    }
}