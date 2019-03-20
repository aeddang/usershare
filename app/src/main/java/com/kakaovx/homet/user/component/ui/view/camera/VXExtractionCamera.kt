package com.kakaovx.homet.user.component.ui.view.camera

import android.Manifest
import android.content.Context
import android.media.Image
import android.util.AttributeSet
import android.util.Log
import com.kakaovx.homet.user.R

class VXExtractionCamera: VXCamera {
    private val TAG = javaClass.simpleName
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun getNeededPermissions():Array<String>{
        return arrayOf( Manifest.permission.CAMERA )
    }

    override fun onCreated() {
        super.onCreated()
        this.isFront = true
        this.captureMode = CaptureMode.Extraction
        this.extractionFps = 5
    }

    override fun onExtraction(image: Image) {
        Log.d(TAG, image.toString())
    }
}