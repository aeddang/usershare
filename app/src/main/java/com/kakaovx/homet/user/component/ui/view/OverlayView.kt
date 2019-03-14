package com.kakaovx.homet.user.component.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.util.*

class OverlayView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private val callbacks = LinkedList<DrawCallback>()

    /**
     * Interface defining the callback for client classes.
     */
    interface DrawCallback {
        fun drawCallback(canvas: Canvas)
    }

    fun addCallback(callback: DrawCallback) {
        callbacks.add(callback)
    }

    @SuppressLint("MissingSuperCall")
    @Synchronized
    override fun draw(canvas: Canvas) {
        for (callback in callbacks) {
            callback.drawCallback(canvas)
        }
    }
}