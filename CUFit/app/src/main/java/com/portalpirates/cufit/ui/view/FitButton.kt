package com.portalpirates.cufit.ui.view

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.util.ColorUtil


class FitButton(context: Context, attrs: AttributeSet?, defStyle: Int) : FrameLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private enum class ButtonType(private val value: Int) {
        PRIMARY(0),
        SECONDARY(1),
        TERTIARY(2);
        companion object {
            private val values = values()
            fun fromValue(value: Int) = values.firstOrNull { it.value == value } ?: PRIMARY
        }
    }

    private enum class IconLocation(private val value: Int) {
        LEFT(0),
        RIGHT(1);
        companion object {
            private val values = values()
            fun fromValue(value: Int) = values.firstOrNull { it.value == value } ?: LEFT
        }
    }

    private val buttonType: ButtonType
    private val iconLocation: IconLocation

    var text: String = ""
        set(value) {
            field = value
            textView.text = value
        }

    var icon: Drawable? = null
        set(value) {
            field = value
            initImageView()
        }

    private val textView: TextView
    private val imageView: ImageView

    private val btnBackgroundColor: Int
        get() = when(buttonType) {
            ButtonType.PRIMARY -> context.getColor(R.color.button_background)
            ButtonType.SECONDARY -> context.getColor(android.R.color.transparent)
            ButtonType.TERTIARY -> context.getColor(android.R.color.transparent)
        }

    private val btnStrokeColor
        get() = when(buttonType) {
            ButtonType.TERTIARY -> context.getColor(android.R.color.transparent)
            else -> context.getColor(R.color.button_background)
        }

    private val btnForegroundColor: Int
        get() = when(buttonType) {
            ButtonType.PRIMARY -> context.getColor(R.color.button_foreground)
            ButtonType.SECONDARY -> context.getColor(R.color.button_background)
            ButtonType.TERTIARY -> context.getColor(R.color.tertiary_btn_text)
        }

    private val rippleColor: Int
        get() = when(buttonType) {
            ButtonType.PRIMARY -> context.getColor(R.color.overlay_light)
            ButtonType.SECONDARY -> context.getColor(R.color.overlay_dark)
            ButtonType.TERTIARY -> context.getColor(android.R.color.transparent)
        }

    private val underlineText: Boolean
        get() = (buttonType == ButtonType.TERTIARY)


    init {
        inflate(context, R.layout.button_layout, this)
        isClickable = true
        isFocusable = true

        textView = findViewById(R.id.btn_text_view)

        // read in attrs
        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.FitButton, defStyle, 0)
        buttonType = ButtonType.fromValue(typedArr.getInt(R.styleable.FitButton_buttonType, 0))
        text = typedArr.getString(R.styleable.FitButton_text) ?: ""
        iconLocation = IconLocation.fromValue(typedArr.getInt(R.styleable.FitButton_iconOn, 0))
        isClickable = typedArr.getBoolean(R.styleable.FitButton_android_clickable, true)

        imageView = when (iconLocation) {
            IconLocation.LEFT -> findViewById(R.id.btn_icon_left)
            IconLocation.RIGHT -> findViewById(R.id.btn_icon_right)
        }

        icon = typedArr.getDrawable(R.styleable.FitButton_icon)
        typedArr.recycle()


        if (text.isEmpty() && icon == null) {
            throw RuntimeException("Button must have text or icon")
        }

        initImageView()
        initTextView()
        setPadding()

        initForeground()
        initBackground()
    }

    private fun setPadding() {
        if (icon != null && text.isEmpty()) { // icon only
            val padding = context.resources.getDimensionPixelOffset(R.dimen.LU_1)
            imageView.setPadding(padding, 0, padding, 0)
        }
        else if (icon == null && text.isNotEmpty()) { // text only
            val padding = context.resources.getDimensionPixelOffset(R.dimen.LU_3)
            textView.setPadding(padding, 0, padding, 0)
        }
        else { // image and text
            val innerPadding = context.resources.getDimensionPixelOffset(R.dimen.LU_2)
            val outerPadding = if (buttonType != ButtonType.TERTIARY) {
                context.resources.getDimensionPixelOffset(R.dimen.LU_3)
            } else {
                context.resources.getDimensionPixelOffset(R.dimen.LU_1_5)
            }

            if (iconLocation == IconLocation.LEFT) {
                imageView.setPadding(outerPadding, 0, 0, 0)
                textView.setPadding(innerPadding, 0, outerPadding, 0)
            } else {
                imageView.setPadding(0, 0, outerPadding, 0)
                textView.setPadding(outerPadding, 0, innerPadding, 0)
            }
        }
    }

    private fun initImageView() {
        icon?.let {
            it.setTint(btnForegroundColor)
            imageView.setImageDrawable(it)
            imageView.visibility = View.VISIBLE
        }
    }

    private fun initTextView() {
        if (text.isNotEmpty()) {
            textView.text = text
            when (buttonType) {
                ButtonType.TERTIARY -> textView.setTextColor(resources.getColorStateList(R.color.tertiary_btn_text, null))
                else -> textView.setTextColor(btnForegroundColor)
            }
            if (underlineText) {
                textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
            textView.visibility = View.VISIBLE
        }
    }

    private fun initForeground() {
        val foregroundDrawable = ContextCompat.getDrawable(context, R.drawable.button_ripple) as RippleDrawable
        val fgShape = foregroundDrawable.findDrawableByLayerId(android.R.id.mask) as GradientDrawable
        fgShape.color = ColorUtil.getSingleColorStateList(rippleColor)
        foregroundDrawable.setColor(fgShape.color)
        foreground = foregroundDrawable
    }

    private fun initBackground() {
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.button_background) as GradientDrawable
        backgroundDrawable.color = ColorUtil.getSingleColorStateList(btnBackgroundColor)
        backgroundDrawable.setStroke(context.resources.getDimensionPixelOffset(R.dimen.LU_0_5), btnStrokeColor)
        background = backgroundDrawable
    }
}