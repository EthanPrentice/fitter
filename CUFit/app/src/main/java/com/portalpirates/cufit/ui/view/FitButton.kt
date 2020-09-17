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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
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

    private enum class ButtonMode(private val value: Int) {
        NORMAL(0),
        INVERTED(1);
        companion object {
            private val values = values()
            fun fromValue(value: Int) = values.firstOrNull { it.value == value } ?: NORMAL
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
    private val buttonMode: ButtonMode
    private val iconLocation: IconLocation

    private val inverted: Boolean
        get() = buttonMode == ButtonMode.INVERTED

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
            ButtonType.PRIMARY -> ColorUtil.getInvertibleColor(context, R.color.component_bg, inverted)
            ButtonType.SECONDARY -> ContextCompat.getColor(context, android.R.color.transparent)
            ButtonType.TERTIARY -> ContextCompat.getColor(context, android.R.color.transparent)
        }

    private val btnStrokeColor
        get() = when(buttonType) {
            ButtonType.TERTIARY -> ContextCompat.getColor(context, android.R.color.transparent)
            else -> ColorUtil.getInvertibleColor(context, R.color.component_bg, inverted)
        }

    private val btnForegroundColor: Int
        get() = when(buttonType) {
            ButtonType.PRIMARY -> ColorUtil.getInvertibleColor(context, R.color.component_fg, inverted)
            ButtonType.SECONDARY -> ColorUtil.getInvertibleColor(context, R.color.component_bg, inverted)
            ButtonType.TERTIARY -> ContextCompat.getColor(context, R.color.tertiary_btn_text)
        }

    private val rippleColor: Int
        get() = when(buttonType) {
            ButtonType.PRIMARY ->  ColorUtil.getInvertibleColor(context, R.color.overlay_light, inverted)
            ButtonType.SECONDARY ->  ColorUtil.getInvertibleColor(context, R.color.overlay_dark, inverted)
            ButtonType.TERTIARY -> ContextCompat.getColor(context, android.R.color.transparent)
        }

    private val underlineText: Boolean
        get() = (buttonType == ButtonType.TERTIARY)


    private val isTintEnabled: Boolean

    init {
        inflate(context, R.layout.button_layout, this)
        isClickable = true
        isFocusable = true

        textView = findViewById(R.id.btn_text_view)

        // read in attrs
        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.FitButton, defStyle, 0)
        buttonType = ButtonType.fromValue(typedArr.getInt(R.styleable.FitButton_buttonType, 0))
        buttonMode = ButtonMode.fromValue(typedArr.getInt(R.styleable.FitButton_buttonMode, 0))
        text = typedArr.getString(R.styleable.FitButton_text) ?: ""
        iconLocation = IconLocation.fromValue(typedArr.getInt(R.styleable.FitButton_iconOn, 0))
        isClickable = typedArr.getBoolean(R.styleable.FitButton_android_clickable, true)
        isTintEnabled = typedArr.getBoolean(R.styleable.FitButton_iconTintEnabled, true)

        imageView = when (iconLocation) {
            IconLocation.LEFT -> findViewById(R.id.btn_icon_left)
            IconLocation.RIGHT -> findViewById(R.id.btn_icon_right)
        }

        icon = typedArr.getDrawable(R.styleable.FitButton_icon)
        typedArr.recycle()

        initImageView()
        initTextView()

        initForeground()
        initBackground()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // if only icon, it must be square bounds
        if (icon != null && text.isEmpty()) { // icon only
            val min = measuredWidth.coerceAtMost(measuredHeight)
            val params = layoutParams
            params.width = min
            params.height = min
            layoutParams = params
        }

        setPadding()

        if (text.isEmpty() && icon == null) {
            throw RuntimeException("Button must have text or icon")
        }
    }

    private fun setPadding() {
        if (icon != null && text.isEmpty()) { // icon only
            val margin = context.resources.getDimensionPixelOffset(R.dimen.LU_1)
            val params = imageView.layoutParams as LinearLayout.LayoutParams
            params.leftMargin = margin
            params.topMargin = margin
            params.rightMargin = margin
            params.bottomMargin = margin
            imageView.layoutParams = params
        }
        else if (icon == null && text.isNotEmpty()) { // text only
            val padding = context.resources.getDimensionPixelOffset(R.dimen.LU_3)
            textView.setPadding(padding, 0, padding, 0)
        }
        else { // image and text
            val innerPadding = context.resources.getDimensionPixelOffset(R.dimen.LU_1)
            val outerPadding = if (buttonType != ButtonType.TERTIARY) {
                context.resources.getDimensionPixelOffset(R.dimen.LU_3)
            } else {
                context.resources.getDimensionPixelOffset(R.dimen.LU_1)
            }

            if (iconLocation == IconLocation.LEFT) {
                imageView.setPadding(outerPadding, 0, 0, 0)
                textView.setPadding(innerPadding, 0, imageView.measuredWidth + innerPadding, 0)
            } else {
                imageView.setPadding(0, 0, outerPadding, 0)
                textView.setPadding(imageView.measuredWidth + innerPadding, 0, innerPadding, 0)
            }
        }
    }

    private fun initImageView() {
        icon?.let {
            if (isTintEnabled) {
                it.setTint(btnForegroundColor)
            }
            imageView.setImageDrawable(it)
            imageView.visibility = View.VISIBLE
        }

        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
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
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.rounded_component_background) as GradientDrawable
        backgroundDrawable.color = ColorUtil.getSingleColorStateList(btnBackgroundColor)
        backgroundDrawable.setStroke(context.resources.getDimensionPixelOffset(R.dimen.LU_0_5), btnStrokeColor)
        background = backgroundDrawable
    }
}