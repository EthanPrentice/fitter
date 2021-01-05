package com.ethanprentice.fitter.datamodel.workout.processing

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.ethanprentice.fitter.datamodel.adt.DataProcessor
import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.measure.Weight
import com.ethanprentice.fitter.datamodel.data.util.Visibility
import com.ethanprentice.fitter.datamodel.data.workout.*
import com.ethanprentice.fitter.datamodel.workout.WorkoutManager
import com.ethanprentice.fitter.datamodel.workout.cloud.WorkoutQueryCloudInterface
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.Exception
import kotlin.collections.HashMap

internal class WorkoutQueryDataProcessor(manager: Manager) : DataProcessor(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    override val cloudInterface: WorkoutQueryCloudInterface
        get() = workoutManager.queryCloudInterface

    fun getWorkoutByUid(uid: String, listener: TaskListener<Workout>) {
        cloudInterface.getWorkoutByUid(uid, object : TaskListener<DocumentSnapshot> {
            // If a workout is found by the CloudInterface, create a Workout document and call the onSuccessListener
            override fun onSuccess(value: DocumentSnapshot) {
                val workout = createWorkoutFromDocument(value)

                if (workout == null) {
                    listener.onFailure(IllegalStateException("Could not create workout from document!!"))
                    return
                }

                listener.onSuccess(workout)
            }
            // If a workout is not found, or one could not be created from the supplied document, run onSuccess with null
            override fun onFailure(e: Exception?) = listener.onFailure(e)
        })
    }

    fun getWorkoutsByOwner(ownerUid: String, listener: TaskListener<List<Workout>>) {
        cloudInterface.getWorkoutsByOwner(ownerUid, object : TaskListener<QuerySnapshot> {
            // If a workout is found by the CloudInterface, create a Workout document and call the onSuccessListener
            override fun onSuccess(value: QuerySnapshot) {
                val workouts = value.mapNotNull { doc -> createWorkoutFromDocument(doc) }

                if (workouts.isEmpty() && value.size() != 0) {
                    listener.onFailure(IllegalStateException("Could not create any workouts from document!!"))
                    return
                }

                listener.onSuccess(workouts)
            }
            // If a workout is not found, or one could not be created from the supplied document, run onSuccess with null
            override fun onFailure(e: Exception?) = listener.onFailure(e)
        })
    }

    fun getWorkoutLogByUid(ownerUid: String, workoutLogUid: String, listener: TaskListener<Workout>) {
        cloudInterface.getWorkoutLogByOwnerIdAndUid(ownerUid, workoutLogUid, object : TaskListener<DocumentSnapshot> {
            // If a workout is found by the CloudInterface, create a Workout document and call the onSuccessListener
            override fun onSuccess(value: DocumentSnapshot) {
                val workoutLog = createWorkoutFromDocument(value)

                if (workoutLog == null) {
                    listener.onFailure(IllegalStateException("Could not create workout log from document!!"))
                    return
                }

                listener.onSuccess(workoutLog)
            }
            // If a workout is not found, or one could not be created from the supplied document, run onSuccess with null
            override fun onFailure(e: Exception?) = listener.onFailure(e)
        })
    }

    fun getWorkoutLogsByOwnerId(ownerUid: String, listener: TaskListener<List<Workout>> ) {
        cloudInterface.getAllWorkoutLogsByOwnerId(ownerUid, object : TaskListener<QuerySnapshot> {
            // If a workout is found by the CloudInterface, create a Workout document and call the onSuccessListener
            override fun onSuccess(value: QuerySnapshot) {
                val workoutLogs = value.mapNotNull { doc -> createWorkoutFromDocument(doc) }

                if (workoutLogs.isEmpty() && value.size() != 0) {
                    listener.onFailure(IllegalStateException("Could not create any workout logs from document!!"))
                    return
                }

                listener.onSuccess(workoutLogs)
            }
            // If a workout is not found, or one could not be created from the supplied document, run onSuccess with null
            override fun onFailure(e: Exception?) = listener.onFailure(e)
        })
    }

    fun getRecentWorkouts(ownerUid: String, listener: TaskListener<List<Workout>>) {
        getWorkoutsByOwner(ownerUid, object : TaskListener<List<Workout>> {
            override fun onSuccess(value: List<Workout>) {
                listener.onSuccess(value.filter { it.dateLogged != null }.sortedByDescending { it.dateLogged })
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })
    }



    @Throws(IllegalArgumentException::class)
    private fun createWorkoutFromDocument(doc: DocumentSnapshot) : Workout? {
        return try {
            val muscleGroups = doc.data?.get(WorkoutField.TARGET_MUSCLE_GROUPS.toString()) as? List<String>
            val exercises = doc.data?.get(WorkoutField.EXERCISES.toString()) as? List<HashMap<String, Any?>>

            WorkoutBuilder()
                .setUid(doc.id)
                .setName(doc.getString(WorkoutField.NAME.toString())!!)
                .setVisibility(Visibility.getFromString(doc.getString(WorkoutField.VISIBILITY.toString())!!)!!)
                .setOwnerUid(doc.getString(WorkoutField.OWNER.toString()))
                .setDescription(doc.getString(WorkoutField.DESCRIPTION.toString()))
                // TODO set subscribers
                .setExercises(exercises?.map { Exercise(it) })
                .setTargetMuscleGroups(muscleGroups?.map { MuscleGroup(it) })
                .setImageBlob(doc.getBlob(WorkoutField.IMAGE_BMP.toString())?.toBytes())
                .setDateLogged(doc.getDate(WorkoutField.DATE_LOGGED.toString()))
                .build()

        } catch (e: Exception) {
            Log.e(TAG, "Could not create a workout from document")
            Log.e(TAG, e.message.toString())
            null
        }
    }


    /**
     * @return weights for logged instances of the exercise with [exerciseName] sorted descending by date logged
     */
    fun getLoggedExerciseWeights(exerciseName: String, ownerUid: String, listener: TaskListener<List<Weight>>) {

        val matchedExerciseWeights: MutableList<Weight> = mutableListOf()

        getWorkoutLogsByOwnerId( ownerUid, object : TaskListener<List<Workout>> {
            override fun onSuccess(value: List<Workout>) {
                value.forEach { workout ->
                    workout.exercises.forEach { exercise ->
                        if (exercise.name.equals(exerciseName)) matchedExerciseWeights.add(exercise.weight!!)
                    }
                }
                matchedExerciseWeights.sortBy { it.dateLogged }
                listener.onSuccess(matchedExerciseWeights)
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })

    }



    companion object {
        private const val TAG = "WorkoutQueryDataProcessor"
    }
}