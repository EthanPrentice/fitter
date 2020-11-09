package com.portalpirates.cufit.datamodel.data.workout

import android.graphics.drawable.Drawable
import com.portalpirates.cufit.datamodel.data.exercise.Exercise
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem

open class Workout(
        var name: String,
        var description: String? = null,
        var owner: FitUser,
        var public: Boolean,
        var subscribers: List<FitUser>? = null,
        var exercises: List<Exercise>? = null,
        var targetMuscleGroups: List<MuscleGroup>? = null
) : SwimlaneItem {
    // copy constructor
    constructor(workout: Workout) : this(
            workout.name,
            workout.description,
            workout.owner,
            workout.public,
            workout.subscribers,
            workout.exercises,
            workout.targetMuscleGroups
    )

    override fun getTitle(): String {
        return name
    }

    override fun getDrawable(): Drawable? {
        return null
    }
}