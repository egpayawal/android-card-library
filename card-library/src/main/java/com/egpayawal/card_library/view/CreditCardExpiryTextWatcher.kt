package com.egpayawal.card_library.view

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by Eraño Payawal on 10/16/20.
 * hunterxer31@gmail.com
 */
class CreditCardExpiryTextWatcher(private val textInputEditText: TextInputEditText) : TextWatcher {
    private var isDelete = false
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        isDelete = before != 0
    }

    override fun afterTextChanged(s: Editable) {
        val source = s.toString()
        val length = source.length
        val stringBuilder = StringBuilder()
        stringBuilder.append(source)
        if (length > 0 && length == 3) {
            if (isDelete) stringBuilder.deleteCharAt(length - 1) else stringBuilder.insert(
                length - 1,
                "/"
            )
            textInputEditText.setText(stringBuilder)
            textInputEditText.setSelection(textInputEditText.text!!.length)
        }
    }
}