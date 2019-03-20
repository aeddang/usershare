package com.kakaovx.homet.user.component.ui.view.camera

import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.user.R
import kotlinx.android.synthetic.main.ui_capture_camera.view.*
import java.io.File

class VXCapcureCamera: VXCamera {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)


    companion object {
        const val CAPTURE_FILE_NAME = "pic.jpg"
    }
    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.ui_capture_camera }
    override fun getNeededPermissions():Array<String>{
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA )
    }

    override fun onCreated() {
        super.onCreated()
        file = File(getActivity().getExternalFilesDir(null), CAPTURE_FILE_NAME)
    }

    override fun onSubscribe() {
        super.onSubscribe()
        btnCapture.clicks().subscribe(this::onTakePicture).apply { disposables?.add(this) }
        btnChangeCamera.clicks().subscribe(this::onToggleCamera).apply { disposables?.add(this) }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onTakePicture(v: Unit) { takePicture() }
    @Suppress("UNUSED_PARAMETER")
    private fun onToggleCamera(v: Unit) { toggleCamera() }

    override fun onCapture() {
        file?.let {
            val bitmap = BitmapFactory.decodeFile( it.absolutePath )
            Log.d(TAG, "file$bitmap")
            this.imageView.setImageBitmap( bitmap )
        }
    }
}