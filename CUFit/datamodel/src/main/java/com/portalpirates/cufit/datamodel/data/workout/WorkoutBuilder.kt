package com.portalpirates.cufit.datamodel.data.workout

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.Blob
import com.portalpirates.cufit.datamodel.FitException
import com.portalpirates.cufit.datamodel.cloud.WorkoutCloudInterface
import com.portalpirates.cufit.datamodel.data.exercise.Exercise
import com.portalpirates.cufit.datamodel.data.musclegroup.MuscleGroup
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import java.io.ByteArrayOutputStream

class WorkoutBuilder {

    // required
    var name: String? = null
    var owner: FitUser? = null
    var public: Boolean? = null

    // optional
    var description: String? = null
    var subscribers: List<FitUser>? = null
    var exercises: List<Exercise>? = null
    var targetMuscleGroups: List<MuscleGroup>? = null
    var imageBmp: Bitmap? = null

    @Throws(WorkoutBuildException::class)
    fun build(): Workout {
        if (hasRequiredInputs()) {
            return Workout(name!!, description, owner!!, public!!, subscribers, exercises, targetMuscleGroups, imageBmp)
        } else {
            throw WorkoutBuildException("All required fields have not been provided for workout! Cannot build!")
        }
    }

    fun setName(name: String): WorkoutBuilder {
        this.name = name
        return this
    }

    fun setOwner(owner: FitUser): WorkoutBuilder {
        this.owner = owner
        return this
    }

    fun setPublic(public: Boolean): WorkoutBuilder {
        this.public = public
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

    fun hasRequiredInputs(): Boolean {
        return name != null &&
                owner != null &&
                public != null
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        val imageBlob = imageBmp?.let { bmp ->
            val bos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)
            Blob.fromBytes(bos.toByteArray())
        }

        val hashMap = hashMapOf<String, Any?>(
                WorkoutCloudInterface.NAME to name,
                WorkoutCloudInterface.OWNER to owner,
                WorkoutCloudInterface.PUBLIC to public,
                WorkoutCloudInterface.IMAGE_BMP to imageBlob,
                // NOT SURE IF THIS IS CORRECT!?!
                WorkoutCloudInterface.DESCRIPTION to description,
                WorkoutCloudInterface.SUBSCRIBERS to subscribers,
                WorkoutCloudInterface.EXERCISES to exercises,
                WorkoutCloudInterface.TARGET_MUSCLE_GROUPS to targetMuscleGroups
        )

        return hashMap
    }

    class WorkoutBuildException(s: String, cause: Throwable?) : FitException(s, cause) {
        constructor(s: String) : this(s, null)
    }
}