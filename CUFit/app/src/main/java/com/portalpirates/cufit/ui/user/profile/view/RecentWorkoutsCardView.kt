package com.portalpirates.cufit.ui.user.profile.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneCardView

class RecentWorkoutsCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : SwimlaneCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun clearSwimlaneItems() {
        swimlaneItems.clear()
        swimlaneItems.add(AddWorkoutItem())
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