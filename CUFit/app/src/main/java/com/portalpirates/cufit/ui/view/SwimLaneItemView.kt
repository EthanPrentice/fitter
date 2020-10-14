package com.portalpirates.cufit.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import de.hdodenhof.circleimageview.CircleImageView

class SwimLaneItemView(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val image = CircleImageView(context)
    val title = TextView(context)

    init {
        makeClickable()

        image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background))
        image.borderWidth = resources.getDimensionPixelOffset(R.dimen.LU_7_5)
        image.borderColor = ContextCompat.getColor(context, R.color.icon_secondary)

        title.text = "This is a very, very, very, very, very, VERY long workout name"

        val imageParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val titleParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addView(image, imageParams)
        addView(title, titleParams)
    }

    private fun makeClickable() {
        isClickable = true
        isFocusable = true
    }
}