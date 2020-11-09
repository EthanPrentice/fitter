package com.portalpirates.cufit.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R

open class FitCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : CardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    protected var contentView: View? = null
        private set
    protected val rootView: ConstraintLayout

    protected val topBarLayout: ConstraintLayout
    protected val titleView: TextView
    protected val statusTextView: TextView
    protected val statusImageView: ImageView

    var topBarVisible: Boolean
        get() = topBarLayout.visibility == View.VISIBLE
        set(value) {
            topBarLayout.visibility = if (value) View.VISIBLE else View.GONE
        }

    var statusColor: Int = ContextCompat.getColor(context, R.color.text_secondary)
        set(value) {
            val drawable = statusImageView.drawable
            if (value == 0) {
                drawable?.setTintList(null)
            } else {
                drawable?.setTint(value)
                statusImageView.setImageDrawable(drawable)

                statusTextView.setTextColor(value)
            }

            field = value
        }

    init {
        rootView = inflate(context, R.layout.fit_card_layout, null) as ConstraintLayout
        addView(rootView)
        topBarLayout = rootView.findViewById(R.id.top_bar)
        titleView = topBarLayout.findViewById(R.id.title)
        statusTextView = topBarLayout.findViewById(R.id.status)
        statusImageView = topBarLayout.findViewById(R.id.status_icon)

        radius = resources.getDimension(R.dimen.LU_2_5)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))

        topBarVisible = false
    }

    protected fun setContentView(view: View, height: Int = ConstraintLayout.LayoutParams.WRAP_CONTENT) {
        val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height).apply {
            topToBottom = topBarLayout.id
            leftToLeft = id
            rightToRight = id
            bottomToBottom = id
        }
        contentView?.let {
            rootView.removeView(it)
        }
        contentView = view
        rootView.addView(view, layoutParams)
    }

    /**
     * Sets title in the [topBarLayout]
     */
    open fun setTitle(title: String) {
        titleView.text = title
    }

    fun setStatusText(text: String) {
        statusTextView.text = text
    }

    fun setStatusIcon(drawable: Drawable?) {
        drawable?.setTint(statusColor)
        statusImageView.setImageDrawable(drawable)
    }
}