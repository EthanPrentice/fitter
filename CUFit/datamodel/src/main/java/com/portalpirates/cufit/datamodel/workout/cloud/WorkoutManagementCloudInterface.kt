package com.portalpirates.cufit.datamodel.workout.cloud

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.adt.CloudInterface
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.datamodel.workout.WorkoutManager
import java.lang.IllegalArgumentException

internal class WorkoutManagementCloudInterface(manager: Manager) : CloudInterface(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    fun createWorkout(fields: HashMap<String, Any?>, listener: TaskListener<String>) {
        db.collection(COLLECTION).add(fields).addOnSuccessListener { ref ->
            updateWorkout(ref.id, fields, object : TaskListener<Unit?> {
                override fun onSuccess(value: Unit?) = listener.onSuccess(ref.id)
                override fun onFailure(e: Exception?) = listener.onFailure(e)
            })

        }.addOnFailureListener { e ->
            Log.w(TAG, "Could not create the workout!")
            listener.onFailure(e)
        }
    }

    fun updateWorkout(uid: String, fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        workoutManager.queryCloudInterface.getWorkoutByUid(uid, object :
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

        private const val COLLECTION = "workouts"
    }
}