package com.portalpirates.cufit.ui.view.swimlane

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SwimlaneView(context: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var visibleItems = 3.25f
    val swimlaneAdapter: SwimlaneAdapter?
        get() = adapter as SwimlaneAdapter?

    init {
        layoutManager = object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: LayoutParams?): Boolean {
                // force width of viewHolder here
                lp?.width = (width / visibleItems).toInt()
                return true
            }

        }
    }

    fun setTextAppearance(resId: Int) {
        swimlaneAdapter?.textAppearance = resId
    }
}