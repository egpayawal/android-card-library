package com.egpayawal.card_library.utils

import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View
import java.util.*

/**
 * Created by Eraño Payawal on 10/16/20.
 * hunterxer31@gmail.com
 */
class CardNumberTransformation : TransformationMethod {
    override fun getTransformation(charSequence: CharSequence, view: View): CharSequence {
        if (charSequence.length >= 9) {
            val result = StringBuilder()
                .append(FOUR_DOTS)
                .append(" ")
                .append(charSequence.subSequence(charSequence.length - 4, charSequence.length))
            val padding = CharArray(charSequence.length - result.length)
            Arrays.fill(padding, Character.MIN_VALUE)
            result.insert(0, padding)
            return result.toString()
        }
        return charSequence
    }

    override fun onFocusChanged(view: View, charSequence: CharSequence, b: Boolean, i: Int, rect: Rect) {}

    companion object {
        private const val FOUR_DOTS = "••••"
    }
}