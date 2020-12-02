package com.portalpirates.cufit.datamodel.data.workout

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem

open class Workout(
        var name: String,
        var description: String? = null,
        var ownerUid: String,
        var owner: FitUser?,
        var public: Boolean,
        var subscribers: List<FitUser>? = null,
        var exercises: List<Exercise>? = null,
        var targetMuscleGroups: List<MuscleGroup>? = null,
        var imageBmp: Bitmap? = null
) : SwimlaneItem {

    // Only null if it hasn't been pushed to the cloud yet.
    var uid: String? = null
        private set

    // copy constructor
    constructor(workout: Workout) : this(
            workout.name,
            workout.description,
            workout.ownerUid,
            workout.owner,
            workout.public,
            workout.subscribers,
            workout.exercises,
            workout.targetMuscleGroups,
            workout.imageBmp
    ) {
        uid = workout.uid
    }


    override fun getTitle(): String {
        return name
    }

    override fun getDrawable(): Drawable? {
        return null
    }
}