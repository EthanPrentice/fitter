package com.portalpirates.cufit.datamodel.data.workout

import android.graphics.drawable.Drawable
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem

class Workout : SwimlaneItem {

    override fun getTitle(): String {
        return "Workout"
    }

    override fun getDrawable(): Drawable? {
        return null
    }
}