package com.portalpirates.cufit.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R

class SingleSelectView(context: Context, attrs: AttributeSet?, defStyle: Int) : CompoundButton(context, attrs, defStyle), Checkable {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        setTextAppearance(R.style.button_text)
        setBackgroundResource(R.drawable.checkable_rounded_bg)
        setTextColor(ContextCompat.getColorStateList(context, R.color.checkable_text))
        foreground = ContextCompat.getDrawable(context, R.drawable.button_ripple)

        minWidth = resources.getDimensionPixelOffset(R.dimen.LU_4_5)
        minHeight = resources.getDimensionPixelOffset(R.dimen.LU_4_5)

        gravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER

        val padding = context.resources.getDimensionPixelOffset(R.dimen.LU_3)
        setPadding(padding, 0, padding, 0)

        setOnClickListener(null)
    }

    // Updates the checked attr for selectors to use
    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(
            android.R.attr.state_checked
        )
    }
}