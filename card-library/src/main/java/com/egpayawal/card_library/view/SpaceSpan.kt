package com.egpayawal.card_library.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.style.ReplacementSpan

/**
 * Created by Eraño Payawal on 10/16/20.
 * hunterxer31@gmail.com
 * A [android.text.style.ReplacementSpan] used for spacing in [android.widget.EditText]
 * to space things out. Adds ' 's
 */
class SpaceSpan : ReplacementSpan {
    private var separator = SEPARATOR_SPACE_WITH_DASH

    constructor() {}
    constructor(separator: String) {
        this.separator = separator
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        val padding = paint.measureText(separator, 0, separator.length)
        val textSize = paint.measureText(text, start, end)
        return (padding + textSize).toInt()
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int,
        bottom: Int, paint: Paint
    ) {
        canvas.drawText(text.subSequence(start, end).toString() + separator, x, y.toFloat(), paint)
    }

    companion object {
        var SEPARATOR_SPACE = " "
        var SEPARATOR_SPACE_WITH_DASH = " - "
    }
}