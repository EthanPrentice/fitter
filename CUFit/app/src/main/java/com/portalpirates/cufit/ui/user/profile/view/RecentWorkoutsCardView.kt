package com.portalpirates.cufit.ui.user.profile.view

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.view.FitCardView
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneAdapter
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneView

class RecentWorkoutsCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val swimlaneItems = ArrayList<SwimlaneItem>()

    private var onItemClickListener: ((View?, SwimlaneItem) -> Unit?)? = null

    init {
        swimlaneItems.add(AddWorkoutItem())

        val swimlaneAdapter = SwimlaneAdapter(context, swimlaneItems, ContextCompat.getDrawable(context, R.drawable.default_workout_img)!!, ::itemOnClick)
        val swimlane = SwimlaneView(context)
        swimlane.adapter = swimlaneAdapter
        swimlane.visibleItems = 4f
        swimlane.setTextAppearance(R.style.subtitle)

        // TODO: change this to be computed instead of hardcoded (hardcoded just for demo)
        setStatusText("2 week streak")

        setContentView(swimlane, (160*1.5f).toInt())
        topBarVisible = true
        setTitle("Workouts")
        statusColor = ContextCompat.getColor(context, R.color.status_green)
    }

    private fun itemOnClick(view: View?, item: SwimlaneItem) {
        if (item is AddWorkoutItem) {
            // TODO: open logging workout screen here
        } else {
            onItemClickListener?.invoke(view, item)
        }
    }

    fun setOnItemClickListener(listener: ((View?, SwimlaneItem) -> Unit?)?) {
        onItemClickListener = listener
    }

    fun setSwimlaneItems(items: List<SwimlaneItem>) {
        clearSwimlaneItems()
        swimlaneItems.addAll(items)
    }

    fun clearSwimlaneItems() {
        swimlaneItems.clear()
        swimlaneItems.add(AddWorkoutItem())
    }

    fun addSwimlaneItems(vararg items: SwimlaneItem) {
        swimlaneItems.addAll(items)
    }

    fun removeSwimlaneItems(vararg items: SwimlaneItem) {
        swimlaneItems.removeAll(items)
    }

    private inner class AddWorkoutItem : SwimlaneItem {
        override fun getTitle(): String {
            return "Add"
        }

        override fun getDrawable(): Drawable? {
            return ContextCompat.getDrawable(context, R.drawable.add_circle)
        }
    }

}