package com.portalpirates.cufit.ui.view

import android.content.Context
import android.util.AttributeSet
import de.hdodenhof.circleimageview.CircleImageView

class FitCircleImageView(context: Context, attrs: AttributeSet?, defStyle: Int) : CircleImageView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = measuredWidth
        var height = measuredHeight

        val widthWithoutPadding = width - paddingLeft - paddingRight
        val heightWidthoutPadding = height - paddingTop - paddingBottom

        if (widthWithoutPadding > heightWidthoutPadding) {
            width = heightWidthoutPadding + paddingLeft + paddingRight
        } else {
            height = widthWithoutPadding + paddingTop + paddingBottom
        }

        setMeasuredDimension(width, height)
    }

}