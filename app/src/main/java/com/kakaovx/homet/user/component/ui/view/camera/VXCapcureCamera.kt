package com.kakaovx.homet.user.component.ui.view.camera

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.util.AttributeSet
import android.util.Log
import kotlinx.android.synthetic.main.ui_camera.view.*
import java.io.File

class VXCapcureCamera: VXCamera {
    companion object {
        const val CAPTURE_FILE_NAME = "pic.jpg"
    }

    private val TAG = javaClass.simpleName
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)



    override fun onCreated() {
        super.onCreated()
        file = File(getActivity().getExternalFilesDir(null), CAPTURE_FILE_NAME)
    }

    override fun onCapture() {
        file?.let {
            val bitmap = BitmapFactory.decodeFile( it.absolutePath )
            Log.d(TAG, "file$bitmap")
            this.imageView.setImageBitmap( bitmap )
        }
    }
}