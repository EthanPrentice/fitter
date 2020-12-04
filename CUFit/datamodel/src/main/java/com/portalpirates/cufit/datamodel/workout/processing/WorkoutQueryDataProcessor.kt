package com.portalpirates.cufit.datamodel.workout.processing

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.*
import com.portalpirates.cufit.datamodel.workout.WorkoutManager
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutQueryCloudInterface
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

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

    fun getWorkoutLogByUid(ownerUid: String, workoutLogUid: String, listener: TaskListener<WorkoutLog>) {
        cloudInterface.getWorkoutLogByOwnerIdAndUid(ownerUid, workoutLogUid, object : TaskListener<DocumentSnapshot> {
            // If a workout is found by the CloudInterface, create a Workout document and call the onSuccessListener
            override fun onSuccess(value: DocumentSnapshot) {
                val workoutLog = createWorkoutLogFromDocument(value)

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

    fun getWorkoutLogsByOwnerAndWorkoutUid( ownerUid: String, workoutUid: String, listener: TaskListener<List<WorkoutLog>> ) {
        cloudInterface.getAllWorkoutLogsByOwnerIdAndWorkoutId(ownerUid, workoutUid, object : TaskListener<QuerySnapshot> {
            // If a workout is found by the CloudInterface, create a Workout document and call the onSuccessListener
            override fun onSuccess(value: QuerySnapshot) {
                val workoutLogs = value.mapNotNull { doc -> createWorkoutLogFromDocument(doc) }

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



    @Throws(IllegalArgumentException::class)
    private fun createWorkoutFromDocument(doc: DocumentSnapshot) : Workout? {
        return try {
            WorkoutBuilder()
                .setUid(doc.id)
                .setName(doc.getString(WorkoutField.NAME.toString())!!)
                .setPublic(doc.getBoolean(WorkoutField.PUBLIC.toString())!!)
                .setOwnerUid(doc.getString(WorkoutField.OWNER.toString()))
                .setDescription(doc.getString(WorkoutField.DESCRIPTION.toString()))
                // TODO set subscribers
                // TODO set exercises
                // TODO set targetmusclegroups
                .setImageBlob(doc.getBlob(WorkoutField.IMAGE_BMP.toString())?.toBytes())
                .build()

        } catch (e: Exception) {
            Log.e(TAG, "Could not create a workout from document")
            null
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun createWorkoutLogFromDocument(doc: DocumentSnapshot) : WorkoutLog? {
        return try {
//            val exercises = doc.data?.get(WorkoutLogField.EXERCISES.toString()) as? List<HashMap<String, Any?>>

            WorkoutLogBuilder()
                    .setUid(doc.id)
                    .setWorkoutUid(doc.getString(WorkoutLogField.WORKOUT_UID.toString())!!)
                    .setOwnerUid(doc.getString(WorkoutLogField.OWNER_UID.toString())!!)
//                    .setExercises()
                    .build()

        } catch (e: Exception) {
            Log.e(TAG, "Could not create a workout from document")
            null
        }
    }

    companion object {
        private const val TAG = "WorkoutQueryDataProcessor"
    }
}