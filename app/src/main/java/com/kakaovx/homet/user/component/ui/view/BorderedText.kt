package com.kakaovx.homet.user.component.ui.view

import android.graphics.*
import com.kakaovx.homet.user.util.Log
import java.util.*

/**
 * A class that encapsulates the tedious bits of rendering legible, bordered text onto a canvas.
 */
class BorderedText {

    val TAG = javaClass.simpleName

    private val interiorPaint: Paint = Paint()
    private val exteriorPaint: Paint = Paint()
    private var textSize: Float = 0.toFloat()

    /**
     * Create a bordered text object with the specified interior and exterior colors, text size and
     * alignment.
     *
     * @param interiorColor the interior text color
     * @param exteriorColor the exterior text color
     * @param textSize text size in pixels
     */
    constructor(interiorColor: Int, exteriorColor: Int, textSize: Float) {
        Log.d(TAG, "BorderedText() textSize = [$textSize]")
        interiorPaint.textSize = textSize
        interiorPaint.color = interiorColor
        interiorPaint.style = Paint.Style.FILL
        interiorPaint.isAntiAlias = false
        interiorPaint.alpha = 255

        exteriorPaint.textSize = textSize
        exteriorPaint.color = exteriorColor
        exteriorPaint.style = Paint.Style.FILL_AND_STROKE
        exteriorPaint.strokeWidth = textSize / 8
        exteriorPaint.isAntiAlias = false
        exteriorPaint.alpha = 255

        this.textSize = textSize
    }

    /**
     * Creates a left-aligned bordered text object with a white interior, and a black exterior with
     * the specified text size.
     *
     * @param textSize text size in pixels
     */
    constructor(textSize: Float) : this(Color.WHITE, Color.BLACK, textSize)

    fun setTypeface(typeface: Typeface) {
        interiorPaint.typeface = typeface
        exteriorPaint.typeface = typeface
    }

    private fun drawText(canvas: Canvas, posX: Float, posY: Float, text: String) {
        canvas.drawText(text, posX, posY, exteriorPaint)
        canvas.drawText(text, posX, posY, interiorPaint)
    }

    fun drawLines(canvas: Canvas, posX: Float, posY: Float, lines: Vector<String>) {
        for ((lineNum, line) in lines.withIndex()) {
            val x = posX
            val y = posY - textSize * (lines.size - lineNum - 1)
//            Log.d(TAG, "drawLines() testSize[$textSize]")
//            Log.d(TAG, "drawLines() lines.size[${lines.size}]")
//            Log.d(TAG, "drawLines() lineNum[$lineNum]")
//            Log.d(TAG, "drawLines() X[$x]")
//            Log.d(TAG, "drawLines() Y[$y]")
//            Log.d(TAG, "drawLines() VALUE[$line]")

            drawText(canvas, x, y, line)
        }
    }

    fun setInteriorColor(color: Int) {
        interiorPaint.color = color
    }

    fun setExteriorColor(color: Int) {
        exteriorPaint.color = color
    }

    fun setAlpha(alpha: Int) {
        interiorPaint.alpha = alpha
        exteriorPaint.alpha = alpha
    }

    fun getTextBounds(
        line: String, index: Int, count: Int, lineBounds: Rect
    ) {
        interiorPaint.getTextBounds(line, index, count, lineBounds)
    }

    fun setTextAlign(align: Paint.Align) {
        interiorPaint.textAlign = align
        exteriorPaint.textAlign = align
    }
}