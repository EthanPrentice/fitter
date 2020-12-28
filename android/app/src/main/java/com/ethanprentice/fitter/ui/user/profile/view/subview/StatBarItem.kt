package com.ethanprentice.fitter.ui.user.profile.view.subview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.ui.view.MeasuredTextView

class StatBarItem(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var title: String = ""
        set(value) {
            titleView.text = value
            field = value
        }

    var value: String = ""
        set(value) {
            valueView.text = value
            field = value
        }

    private val measured: Boolean

    private val titleView = TextView(context)
    private val valueView: TextView

    init {
        orientation = VERTICAL

        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.StatBarItem, defStyle, 0)

        measured = typedArr.getBoolean(R.styleable.StatBarItem_measured, false)
        valueView = if (measured) {
            MeasuredTextView(context)
        } else {
            TextView(context)
        }

        title = typedArr.getString(R.styleable.StatBarItem_title) ?: ""
        value = typedArr.getString(R.styleable.StatBarItem_value) ?: ""
        typedArr.recycle()

        initPadding()
        initTextAppearances()

        titleView.gravity = Gravity.CENTER_HORIZONTAL
        valueView.gravity = Gravity.CENTER_HORIZONTAL

        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.LU_2)
        setPadding(paddingHorizontal, 0, paddingHorizontal, 0)

        addView(titleView)
        addView(valueView)
    }

    private fun initTextAppearances() {
        titleView.setTextAppearance(R.style.subtitle)
        titleView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        titleView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))

        valueView.setTextAppearance(R.style.header3)
        valueView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        valueView.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
    }

    private fun initPadding() {
        titleView.setPadding(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.LU_0_5))
    }

    fun setValue(n: Number) {
        value = n.toString()
    }
}