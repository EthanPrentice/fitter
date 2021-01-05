package com.ethanprentice.fitter.datamodel.data.workout

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.ethanprentice.fitter.datamodel.data.user.FitUser
import com.ethanprentice.fitter.datamodel.data.util.SwimlaneItem
import com.ethanprentice.fitter.datamodel.data.util.Visibility
import java.util.*

open class Workout(
        var name: String,
        var description: String? = null,
        var ownerUid: String,
        var owner: FitUser?,
        var visibility: Visibility,
        var subscribers: List<FitUser>? = null,
        var exercises: MutableList<Exercise>,
        var targetMuscleGroups: List<MuscleGroup>? = null,
        var imageBmp: Bitmap? = null,
        var dateLogged: Date? = null
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
        workout.visibility,
        workout.subscribers,
        workout.exercises,
        workout.targetMuscleGroups,
        workout.imageBmp,
        workout.dateLogged
    ) {
        uid = workout.uid
    }

    constructor(
        uid: String?,
        name: String,
        description: String? = null,
        ownerUid: String,
        owner: FitUser?,
        visibility: Visibility,
        subscribers: List<FitUser>? = null,
        exercises: MutableList<Exercise>,
        targetMuscleGroups: List<MuscleGroup>? = null,
        imageBmp: Bitmap? = null,
        dateLogged: Date? = null
    ) : this(
        name,
        description,
        ownerUid,
        owner,
        visibility,
        subscribers,
        exercises,
        targetMuscleGroups,
        imageBmp,
        dateLogged
    ) {
        this.uid = uid
    }

    override fun getTitle(): String {
        return name
    }

    override fun getDrawable(): Drawable? {
        return if (imageBmp == null) {
            null
        } else {
            BitmapDrawable(imageBmp)
        }
    }
}