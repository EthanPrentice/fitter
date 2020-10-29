package com.portalpirates.cufit.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R

open class FitCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : CardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        radius = resources.getDimension(R.dimen.LU_2_5)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
    }
}