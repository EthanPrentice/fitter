package com.portalpirates.cufit.ui.view.swimlane

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import de.hdodenhof.circleimageview.CircleImageView

class SwimlaneItemView(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val imageView = CircleImageView(context)
    val titleView = TextView(context)

    var text: String
        get() = titleView.text.toString()
        set(value) {
            titleView.text = value
        }

    init {
        makeClickable()

        // read attrs
        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.SwimlaneItemView, defStyle, 0)
        text = typedArr.getString(R.styleable.SwimlaneItemView_text) ?: "ERROR"
        val imageResId = typedArr.getResourceId(R.styleable.SwimlaneItemView_image, 0)
        if (imageResId != 0) {
            setImageResource(imageResId)
        }
        val textColor = typedArr.getResourceId(R.styleable.SwimlaneItemView_android_textColor, R.color.text_secondary)
        typedArr.recycle()

        // set LinearLayout properties
        orientation = VERTICAL
        gravity = Gravity.CENTER

        // set CircleImageView properties
        imageView.setCircleBackgroundColorResource(R.color.icon_secondary)
        imageView.borderWidth = resources.getDimensionPixelOffset(R.dimen.LU_0)

        // set TextView properties
        titleView.setTextAppearance(R.style.body_text)
        titleView.setTextColor(ContextCompat.getColor(context, textColor))
        titleView.maxLines = 2
        titleView.textAlignment = TEXT_ALIGNMENT_CENTER
        titleView.ellipsize = TextUtils.TruncateAt.END

        val imageParams = LayoutParams(LayoutParams.MATCH_PARENT, 0).apply {
            bottomMargin = resources.getDimensionPixelOffset(R.dimen.LU_1)
            weight = 1f
        }
        val titleParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        addView(imageView, imageParams)
        addView(titleView, titleParams)
    }

    fun setImageBitmap(bmp: Bitmap?) {
        imageView.setImageBitmap(bmp)
    }

    fun setImageDrawable(drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }

    fun setImageResource(resId: Int) {
        imageView.setImageResource(resId)
    }

    private fun makeClickable() {
        isClickable = true
        isFocusable = true
    }
}