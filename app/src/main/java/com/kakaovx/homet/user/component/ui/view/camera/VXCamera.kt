package com.kakaovx.homet.user.component.ui.view.camera

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.FragmentActivity
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.camera.AutoFitTextureView
import com.kakaovx.homet.user.component.ui.skeleton.view.camera.Camera
import com.kakaovx.homet.user.ui.PageID
import kotlinx.android.synthetic.main.ui_capture_camera.view.*


abstract class VXCamera: Camera {
    private val TAG = javaClass.simpleName
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    override fun getTextureView(): AutoFitTextureView { return texture }
    override fun getActivity(): FragmentActivity {  return PagePresenter.getInstance<PageID>().activity as FragmentActivity }
    override fun getLayoutResId(): Int { return R.layout.ui_camera }
}