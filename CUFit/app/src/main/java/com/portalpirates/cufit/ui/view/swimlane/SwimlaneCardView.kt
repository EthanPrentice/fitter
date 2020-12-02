package com.portalpirates.cufit.ui.view.swimlane

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.view.FitCardView

open class SwimlaneCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var onItemClickListener: ((View?, SwimlaneItem) -> Unit?)? = null

    private val swimlane = SwimlaneView(context)

    private var items: List<SwimlaneItem>? = null
    var adapter: SwimlaneAdapter? = null
        private set(value) {
            field = value
            swimlane.adapter = value
        }

    init {
        swimlane.visibleItems = 4f
        swimlane.setTextAppearance(R.style.subtitle)

        setContentView(swimlane, (160*1.5f).toInt())
        topBarVisible = true
        statusColor = ContextCompat.getColor(context, R.color.status_green)
    }

    private fun itemOnClick(view: View?, item: SwimlaneItem) {
        onItemClickListener?.invoke(view, item)
    }

    fun setOnItemClickListener(listener: ((View?, SwimlaneItem) -> Unit?)?) {
        onItemClickListener = listener
    }

    fun setSwimlaneItems(items: List<SwimlaneItem>) {
        this.items = items
        adapter = SwimlaneAdapter(context, items, ContextCompat.getDrawable(context, R.drawable.default_workout_img)!!, ::itemOnClick)
    }

}