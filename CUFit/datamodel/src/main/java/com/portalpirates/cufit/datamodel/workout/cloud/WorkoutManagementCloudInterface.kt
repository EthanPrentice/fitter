package com.portalpirates.cufit.datamodel.workout.cloud

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.adt.CloudInterface
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.UserField
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.datamodel.data.workout.WorkoutLogField
import com.portalpirates.cufit.datamodel.workout.WorkoutManager
import java.lang.IllegalArgumentException

internal class WorkoutManagementCloudInterface(manager: Manager) : CloudInterface(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    fun createWorkout(fields: HashMap<String, Any?>, listener: TaskListener<String>) {
        db.collection(COLLECTION).add(fields).addOnSuccessListener { ref ->
            fields[WorkoutField.UID.toString()] = ref.id
            updateWorkout(fields, object : TaskListener<Unit?> {
                override fun onSuccess(value: Unit?) = listener.onSuccess(ref.id)
                override fun onFailure(e: Exception?) = listener.onFailure(e)
            })

        }.addOnFailureListener { e ->
            Log.w(TAG, "Could not create the workout!")
            listener.onFailure(e)
        }
    }

    fun updateWorkout(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        val uid = fields[WorkoutField.UID.toString()] as String?

        if (uid == null) {
            listener.onFailure(IllegalArgumentException("Cannot update a workout with no UID!"))
            return
        }

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

    fun createWorkoutLog( fields: HashMap<String, Any?>, listener: TaskListener<String>) {
        val owner_id = fields[WorkoutLogField.OWNER_UID.toString()] as String?

        if (owner_id == null) {
            listener.onFailure(IllegalArgumentException("Cannot update a workout with no Owner UID!"))
            return
        }

        db.collection(USER_COLLECTION).document(owner_id)
                .collection(WORKOUT_LOGS).add(fields)
            .addOnSuccessListener { ref ->
            fields[WorkoutField.UID.toString()] = ref.id
            updateWorkoutLog(fields, object : TaskListener<Unit?> {
                override fun onSuccess(value: Unit?) = listener.onSuccess(ref.id)
                override fun onFailure(e: Exception?) = listener.onFailure(e)
            })

        }.addOnFailureListener { e ->
            Log.w(TAG, "Could not create the workout!")
            listener.onFailure(e)
        }
    }

    fun updateWorkoutLog(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        val uid = fields[WorkoutField.UID.toString()] as String?
        val owner = fields[WorkoutField.OWNER.toString()] as String?

        if (uid == null) {
            listener.onFailure(IllegalArgumentException("Cannot update a workout log with no UID!"))
            return
        }

        if (owner == null ) {
            listener.onFailure(IllegalArgumentException("Cannot update a workout log with no owner id!"))
            return
        }

        workoutManager.queryCloudInterface.getWorkoutLogByOwnerIdAndUid(owner, uid, object :
                TaskListener<DocumentSnapshot> {
            override fun onSuccess(value: DocumentSnapshot) {
                value.reference.update(fields)
                        .addOnSuccessListener {
                            Log.d(TAG, "Workout log successfully updated.")
                            listener.onSuccess(null)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating Workout log", e)
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
        private const val USER_COLLECTION = "users"
        private const val WORKOUT_LOGS = "workout_logs"
    }
}