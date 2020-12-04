package com.portalpirates.cufit.datamodel.data.workout

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import java.util.*

open class WorkoutLog(
        var owner: String? = null,
        var exercises: List<Exercise>? = null,
        var date: Date? = null
) : SwimlaneItem {

    // Only null if it hasn't been pushed to the cloud yet.
    var uid: String? = null
        private set

    // copy constructor
    constructor(workout: WorkoutLog) : this(
            workout.owner,
            workout.exercises,
            workout.date
    ) {
        uid = workout.uid
    }


    override fun getTitle(): String {
        return date.toString() // Temporary to override this
    }

    override fun getDrawable(): Drawable? {
        return null
    }
}