package com.portalpirates.cufit.datamodel.data.workout

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.Blob
import com.portalpirates.cufit.datamodel.FitException
import com.portalpirates.cufit.datamodel.data.user.FitUser
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class WorkoutLogBuilder {

    // required
    var workoutUid: String? = null
    var ownerUid: String? = null

    // optional
    var exercises: List<Exercise>? = null

    // should only ever be null for workouts not yet committed to the cloud!!
    var uid: String? = null
    var date: Date? = null


    @Throws(WorkoutBuildException::class)
    fun build(): WorkoutLog {
        if (hasRequiredInputs()) {
            date = Date()
            return WorkoutLog(ownerUid, workoutUid, exercises, date)
        } else {
            throw WorkoutBuildException("All required fields have not been provided for workout! Cannot build!")
        }
    }

    fun setUid(uid: String?): WorkoutLogBuilder {
        this.uid = uid
        return this
    }

    fun setExercises(exercises: List<Exercise>?): WorkoutLogBuilder {
        this.exercises = exercises
        return this
    }

    fun setWorkoutUid( workoutUid: String ): WorkoutLogBuilder {
        this.workoutUid = workoutUid
        return this
    }

    fun setOwnerUid( ownerUid: String ) : WorkoutLogBuilder {
        this.ownerUid = ownerUid
        return this
    }

    fun hasRequiredInputs(): Boolean {
        return true
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {

        return hashMapOf<String, Any?>(
                WorkoutLogField.OWNER_UID.toString() to ownerUid,
                WorkoutLogField.EXERCISES.toString() to exercises,
                WorkoutLogField.DATE.toString() to date
        )
    }

    class WorkoutBuildException(s: String, cause: Throwable?) : FitException(s, cause) {
        constructor(s: String) : this(s, null)
    }
}