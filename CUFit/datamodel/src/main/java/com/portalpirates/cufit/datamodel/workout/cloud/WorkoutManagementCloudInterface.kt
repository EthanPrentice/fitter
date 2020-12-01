package com.portalpirates.cufit.datamodel.workout.cloud

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.adt.CloudInterface
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

internal class WorkoutManagementCloudInterface(manager: Manager) : CloudInterface(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    fun createWorkout(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        workoutManager.queryCloudInterface.getWorkoutByUid("TEST", object :
            TaskListener<DocumentSnapshot> {
            override fun onSuccess(value: DocumentSnapshot) {
                value.reference.update(fields)
                    .addOnSuccessListener {
                        Log.d(TAG, "Workout successfully created.")
                        listener.onSuccess(null)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error creating Workout", e)
                        listener.onFailure(e)
                    }
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })
    }

    fun updateWorkout(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        workoutManager.queryCloudInterface.getWorkoutByUid("TEST", object :
            TaskListener<DocumentSnapshot> {
            override fun onSuccess(value: DocumentSnapshot) {
                value.reference.update(fields)
                    .addOnSuccessListener {
                        Log.d(TAG, "Workout successfully updated.")
                        listener.onSuccess(null)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating Workout", e)
                        listener.onFailure(e)
                    }
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })
    }

    companion object {
        private const val TAG = "WorkoutManagementCloudInterface"
    }
}