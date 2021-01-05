package com.ethanprentice.fitter.datamodel.data.workout

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.Blob
import com.ethanprentice.fitter.datamodel.FitException
import com.ethanprentice.fitter.datamodel.data.user.FitUser
import com.ethanprentice.fitter.datamodel.data.util.Visibility
import com.ethanprentice.fitter.datamodel.util.ImageUtil
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WorkoutBuilder() {

    // required
    var name: String? = null

    var owner: FitUser? = null
        set(value) {
            field = value
            ownerUid = value?.uid
        }
    var ownerUid: String? = null

    var visibility: Visibility = Visibility.PUBLIC


    // optional
    var description: String? = null
    var subscribers: List<FitUser>? = null
    var exercises: List<Exercise>? = null
    var targetMuscleGroups: List<MuscleGroup>? = null
    var imageBmp: Bitmap? = null
    var dateLogged: Date? = null

    // should only ever be null for workouts not yet committed to the cloud!!
    var uid: String? = null


    constructor(workout: Workout) : this() {
        setUid(workout.uid)
        setName(workout.name)
        setOwner(workout.owner)
        setOwnerUid(workout.ownerUid)
        setVisibility(workout.visibility)
        setDescription(workout.description)
        setSubscribers(workout.subscribers)
        setExercises(workout.exercises)
        setTargetMuscleGroups(workout.targetMuscleGroups)
        setImage(workout.imageBmp)
        setDateLogged(workout.dateLogged)
    }


    @Throws(WorkoutBuildException::class)
    fun build(): Workout {
        if (hasRequiredInputs()) {
            return Workout(uid, name!!, description, ownerUid!!, owner, visibility, subscribers, exercises?.toMutableList() ?: ArrayList(), targetMuscleGroups, imageBmp, dateLogged)
        } else {
            throw WorkoutBuildException("All required fields have not been provided for workout! Cannot build!")
        }
    }

    fun setUid(uid: String?): WorkoutBuilder {
        this.uid = uid
        return this
    }

    fun setName(name: String): WorkoutBuilder {
        this.name = name
        return this
    }

    fun setOwner(owner: FitUser?): WorkoutBuilder {
        this.owner = owner
        return this
    }

    fun setOwnerUid(uid: String?): WorkoutBuilder {
        this.ownerUid = uid
        return this
    }

    fun setVisibility(visibility: Visibility): WorkoutBuilder {
        this.visibility = visibility
        return this
    }

    fun setDescription(description: String?): WorkoutBuilder {
        this.description = description
        return this
    }

    fun setSubscribers(subscribers: List<FitUser>?): WorkoutBuilder {
        this.subscribers = subscribers
        return this
    }

    fun setExercises(exercises: List<Exercise>?): WorkoutBuilder {
        this.exercises = exercises
        return this
    }

    fun setTargetMuscleGroups(targetMuscleGroups: List<MuscleGroup>?): WorkoutBuilder {
        this.targetMuscleGroups = targetMuscleGroups
        return this
    }

    fun setImage(bmp: Bitmap?): WorkoutBuilder {
        this.imageBmp = bmp
        return this
    }

    fun setImageBlob(byteArr: ByteArray?): WorkoutBuilder {
        if (byteArr == null) {
            imageBmp = null
            return this
        }

        val options = BitmapFactory.Options().apply {
            inMutable = true
        }
        imageBmp = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.size, options)
        return this
    }

    fun setDateLogged(date: Date?): WorkoutBuilder {
        dateLogged = date
        return this
    }

    fun hasRequiredInputs(): Boolean {
        return !name.isNullOrBlank() &&
                !ownerUid.isNullOrBlank() &&
                visibility != null
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        val imageBlob = imageBmp?.let { bmp ->
            val bos = ByteArrayOutputStream()
            ImageUtil.getResizedBitmap(bmp, ImageUtil.MAX_BMP_SIZE).compress(Bitmap.CompressFormat.PNG, 100, bos)
            Blob.fromBytes(bos.toByteArray())
        }

        return hashMapOf<String, Any?>(
            WorkoutField.UID.toString() to uid,
            WorkoutField.NAME.toString() to name,
            WorkoutField.OWNER.toString() to ownerUid,
            WorkoutField.OWNER_REF.toString() to "/users/$ownerUid",
            WorkoutField.VISIBILITY.toString() to visibility.toString(),
            WorkoutField.IMAGE_BMP.toString() to imageBlob,
            WorkoutField.DESCRIPTION.toString() to description,
            WorkoutField.SUBSCRIBERS.toString() to subscribers,
            WorkoutField.EXERCISES.toString() to exercises?.map { it.convertFieldsToHashMap() },
            WorkoutField.TARGET_MUSCLE_GROUPS.toString() to targetMuscleGroups?.map { it.toString() },
            WorkoutField.DATE_LOGGED.toString() to dateLogged
        )
    }

    class WorkoutBuildException(s: String, cause: Throwable?) : FitException(s, cause) {
        constructor(s: String) : this(s, null)
    }
}