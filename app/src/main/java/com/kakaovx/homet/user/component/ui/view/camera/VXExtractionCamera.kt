package com.kakaovx.homet.user.component.ui.view.camera

import android.content.Context
import android.media.Image
import android.util.AttributeSet
import android.util.Log

class VXExtractionCamera: VXCamera {
    private val TAG = javaClass.simpleName
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onCreated() {
        super.onCreated()
        this.isFront = true
        this.captureMode = CaptureMode.Extraction
        this.extractionFps = 15
    }

    override fun onExtraction(image: Image) {
        Log.d(TAG, image.toString())
    }
}