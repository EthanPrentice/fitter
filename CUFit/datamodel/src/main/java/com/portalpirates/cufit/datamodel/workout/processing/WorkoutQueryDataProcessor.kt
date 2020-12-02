package com.portalpirates.cufit.datamodel.workout.processing

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.data.workout.WorkoutBuilder
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
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

    companion object {
        private const val TAG = "WorkoutQueryDataProcessor"
    }
}