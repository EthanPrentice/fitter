package com.portalpirates.cufit.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.util.ColorUtil

class FitEditText(context: Context, attrs: AttributeSet?, defStyle: Int) : TextInputLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private enum class EditTextType(private val value: Int) {
        PRIMARY(0),
        SECONDARY(1);
        companion object {
            private val values = values()
            fun fromValue(value: Int) = values.firstOrNull { it.value == value } ?: PRIMARY
        }
    }

    private val backgroundTint: Int
        get() = when(editTextType) {
            EditTextType.PRIMARY -> ContextCompat.getColor(context, R.color.component_bg)
            EditTextType.SECONDARY -> ContextCompat.getColor(context, android.R.color.transparent)
        }

    private val textColor: Int
        get() = when(editTextType) {
            EditTextType.PRIMARY -> ContextCompat.getColor(context, R.color.component_fg)
            EditTextType.SECONDARY -> ContextCompat.getColor(context, R.color.text_primary)
        }

    private val hintColor: Int
        get() = when(editTextType) {
            EditTextType.PRIMARY -> ContextCompat.getColor(context, R.color.component_fg_secondary)
            EditTextType.SECONDARY -> ContextCompat.getColor(context, R.color.text_secondary)
        }


    private val editTextType: EditTextType
    private val editText: TextInputEditText = TextInputEditText(context)

    var text: String
        get() = editText.text?.toString() ?: ""
        set(value) = editText.setText(value)

    init {
        addView(editText)

        // read in attrs
        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.FitEditText, defStyle, 0)
        editTextType = EditTextType.fromValue(typedArr.getInt(R.styleable.FitEditText_editTextType, 0))
        text = typedArr.getString(R.styleable.FitEditText_android_text) ?: ""
        editText.hint = typedArr.getString(R.styleable.FitEditText_android_hint) ?: ""
        editText.inputType = typedArr.getInt(R.styleable.FitEditText_android_inputType, 0)
        editText.imeOptions = typedArr.getInt(R.styleable.FitEditText_android_imeOptions, 0)
        typedArr.recycle()

        isClickable = true
        isFocusable = true
        isHintEnabled = false

        gravity = Gravity.CENTER_VERTICAL

        editText.setTextAppearance(R.style.edittext_text)

        setEndIconTintList(ContextCompat.getColorStateList(context, R.color.password_hint_tint))

        setPadding()
        initBackground()
        initEditText()
    }


    private fun initBackground() {
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.rounded_component_background) as GradientDrawable
        backgroundDrawable.color = ColorUtil.getSingleColorStateList(backgroundTint)
        background = backgroundDrawable
    }

    private fun initEditText() {
        editText.setHintTextColor(hintColor)
        editText.setTextColor(textColor)
        editText.background = null
    }

    private fun setPadding() {
        val horizontal = resources.getDimensionPixelOffset(R.dimen.LU_3_5)
        setPadding(horizontal, 0, horizontal, 0)
    }

}