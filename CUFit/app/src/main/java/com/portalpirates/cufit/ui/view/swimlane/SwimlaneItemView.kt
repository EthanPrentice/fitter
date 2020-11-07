package com.portalpirates.cufit.ui.view.swimlane

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.view.FitCircleImageView

class SwimlaneItemView(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val rippleView = ImageView(context)
    val imageView = FitCircleImageView(context)
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
        val textColor = typedArr.getResourceId(R.styleable.SwimlaneItemView_android_textColor, R.color.text_primary)
        typedArr.recycle()

        // set LinearLayout properties
        orientation = VERTICAL
        gravity = Gravity.CENTER

        rippleView.setBackgroundResource(R.drawable.circular_ripple)
        rippleView.isDuplicateParentStateEnabled

        // set TextView properties
        titleView.setTextAppearance(R.style.body_text)
        titleView.setTextColor(ContextCompat.getColor(context, textColor))
        titleView.maxLines = 2
        titleView.textAlignment = TEXT_ALIGNMENT_CENTER
        titleView.ellipsize = TextUtils.TruncateAt.END
        titleView.isDuplicateParentStateEnabled = true

        val imageParams = LayoutParams(LayoutParams.WRAP_CONTENT, 0).apply {
            bottomMargin = resources.getDimensionPixelOffset(R.dimen.LU_1)
            weight = 1f
            gravity = Gravity.CENTER_HORIZONTAL
        }
        val titleParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val frameLayout = FrameLayout(context).apply {
            addView(imageView)
            addView(rippleView)
            isDuplicateParentStateEnabled = true
        }

        addView(frameLayout, imageParams)
        addView(titleView, titleParams)
    }
//
//    override fun performClick(): Boolean {
//        rippleView.performClick()
//        return super.performClick()
//    }

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