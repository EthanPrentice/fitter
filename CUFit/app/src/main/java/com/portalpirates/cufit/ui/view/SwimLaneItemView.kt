package com.portalpirates.cufit.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
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

        // set LinearLayout properties
        orientation = VERTICAL
        gravity = Gravity.CENTER

        // set CircleImageView properties
        image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background))
        image.borderWidth = resources.getDimensionPixelOffset(R.dimen.LU_0)
        image.setPadding(0, 0, 0, 30)

        // set TextView properties
        title.text = "Ultimate Full Body"
        title.textSize = resources.getDimensionPixelOffset(R.dimen.swim_lane_text_size).toFloat()
        title.maxLines = 2
        title.maxWidth = 300
        title.textAlignment = TEXT_ALIGNMENT_CENTER
        title.ellipsize = TextUtils.TruncateAt.END

        val imageParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val titleParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addView(image, imageParams)
        addView(title, titleParams)
    }

    private fun makeClickable() {
        isClickable = true
        isFocusable = true
    }
}