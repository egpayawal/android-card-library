package com.egpayawal.card_library.view

import android.content.Context
import android.graphics.Rect
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.TextViewCompat
import com.egpayawal.card_library.R
import com.egpayawal.card_library.utils.CardNumberTransformation
import com.egpayawal.card_library.utils.CardType
import com.egpayawal.card_library.utils.CardType.Companion.forCardNumber
import com.egpayawal.card_library.view.SpaceSpan.Companion.SEPARATOR_SPACE
import com.egpayawal.card_library.view.SpaceSpan.Companion.SEPARATOR_SPACE_WITH_DASH
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by Eraño Payawal on 10/16/20.
 * hunterxer31@gmail.com
 */
class CardTextInputEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs), TextWatcher {

    enum class SeparatorState {
        SPACE, SPACE_WITH_DASH
    }

    interface OnCardTypeChangedListener {
        fun onCardTypeChanged(cardType: CardType?)
    }

    private var mDisplayCardIcon: Boolean
    private var mSeparatorType: String = SEPARATOR_SPACE_WITH_DASH
    private var mMask = false

    /**
     * @return The [com.egpayawal.card_library.utils.CardType] currently entered in
     * the [android.widget.EditText]
     */
    var cardType: CardType? = null
        private set
    private var mOnCardTypeChangedListener: OnCardTypeChangedListener? = null
    private var mSavedTransformationMethod: TransformationMethod? = null
    /**
     * @return If this optional or not. See [.setOptional].
     */
    /**
     * Set as optional. Optional fields are always valid and show no
     * error message.
     *
     * @param optional `true` to set this to optional, `false`
     * to set it to required.
     */
    var isOptional = false

    init {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardTextInputEditText)
        mDisplayCardIcon = attributes.getBoolean(R.styleable.CardTextInputEditText_displayCardIcon, true)
        val separatorState: SeparatorState = SeparatorState.values()[attributes.getInt(R.styleable.CardTextInputEditText_separatorState, 1)]
        setSeparatorState(separatorState)

        inputType = InputType.TYPE_CLASS_NUMBER
        setCardIcon(R.drawable.ic_unknown)
        addTextChangedListener(this)
        updateCardType()
        mSavedTransformationMethod = transformationMethod

        attributes.recycle()
    }

    /**
     * Enable or disable showing card type icons as part of the [CardTextInputEditText]. Defaults to
     * `true`.
     *
     * @param display `true` to display card type icons, `false` to never display card
     * type icons.
     */
    fun displayCardTypeIcon(display: Boolean) {
        mDisplayCardIcon = display
        if (!mDisplayCardIcon) {
            setCardIcon(-1)
        }
    }

    /**
     * @param mask if `true`, all but the last four digits of the card number will be masked when
     * focus leaves the card field. Uses [CardNumberTransformation], transforming the number from
     * something like "4111111111111111" to "•••• 1111".
     */
    fun setMask(mask: Boolean) {
        mMask = mask
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            unmaskNumber()
            if (text.toString().isNotEmpty()) {
                setSelection(text.toString().length)
            }
        } else if (mMask && isValid) {
            maskNumber()
        }
    }

    /**
     * Receive a callback when the [com.egpayawal.card_library.utils.CardType] changes
     * @param listener to be called when the [com.egpayawal.card_library.utils.CardType]
     * changes
     */
    fun setOnCardTypeChangedListener(listener: OnCardTypeChangedListener?) {
        mOnCardTypeChangedListener = listener
    }

    private fun maskNumber() {
        if (transformationMethod !is CardNumberTransformation) {
            mSavedTransformationMethod = transformationMethod
            transformationMethod = CardNumberTransformation()
        }
    }

    private fun unmaskNumber() {
        if (transformationMethod !== mSavedTransformationMethod) {
            transformationMethod = mSavedTransformationMethod
        }
    }

    private fun updateCardType() {
        val type = forCardNumber(text.toString())
        if (cardType !== type) {
            cardType = type
            val filters = arrayOf<InputFilter>(LengthFilter(cardType!!.maxCardLength))
            setFilters(filters)
            invalidate()
            if (mOnCardTypeChangedListener != null) {
                mOnCardTypeChangedListener!!.onCardTypeChanged(cardType)
            }
        }
    }

    private fun addSpans(editable: Editable, spaceIndices: IntArray) {
        val length = editable.length
        for (index in spaceIndices) {
            if (index <= length) {
                editable.setSpan(
                    SpaceSpan(mSeparatorType), index - 1, index,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun setCardIcon(icon: Int) {
        if (!mDisplayCardIcon || text!!.isEmpty()) {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, 0, 0, 0, 0)
        } else {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, 0, 0, icon, 0)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(editable: Editable) {
        val paddingSpans: Array<SpaceSpan> = editable.getSpans(0, editable.length, SpaceSpan::class.java)
        for (span in paddingSpans) {
            editable.removeSpan(span)
        }
        updateCardType()
        setCardIcon(cardType!!.frontResource)
        addSpans(editable, cardType!!.spaceIndices)
        if (cardType!!.maxCardLength == selectionStart) {
            validate()
            if (isValid) {
                focusNextView()
            } else {
                unmaskNumber()
            }
        } else if (!hasFocus()) {
            if (mMask) {
                maskNumber()
            }
        }
    }
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    /**
     * Request focus for the next view.
     */
    fun focusNextView(): View? {
        if (imeActionId == EditorInfo.IME_ACTION_GO) {
            return null
        }
        val next: View?
        next = try {
            focusSearch(View.FOCUS_FORWARD)
        } catch (e: IllegalArgumentException) {
            // View.FOCUS_FORWARD results in a crash in some versions of Android
            // https://github.com/braintree/braintree_android/issues/20
            focusSearch(FOCUS_DOWN)
        }
        return if (next != null && next.requestFocus()) {
            next
        } else null
    }

    val isValid: Boolean
        get() = isOptional || cardType!!.validate(text.toString())

    /**
     * Check if the valid and set the correct error state and visual
     * indication on it.
     */
    fun validate() {
        if (isValid || isOptional) {
            error = null
        } /* else {
            setError(getErrorMessage());
        }*/
    }

    fun setSeparatorState(state: SeparatorState) {
        mSeparatorType = when (state) {
            SeparatorState.SPACE -> {
                SEPARATOR_SPACE
            }
            SeparatorState.SPACE_WITH_DASH -> {
                SEPARATOR_SPACE_WITH_DASH
            }
        }
    }
}