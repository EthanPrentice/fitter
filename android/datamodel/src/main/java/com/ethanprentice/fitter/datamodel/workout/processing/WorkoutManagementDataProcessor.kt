package com.ethanprentice.fitter.datamodel.workout.processing

import android.util.Log
import com.ethanprentice.fitter.datamodel.adt.DataProcessor
import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.workout.Exercise
import com.ethanprentice.fitter.datamodel.data.workout.Workout
import com.ethanprentice.fitter.datamodel.data.workout.WorkoutBuilder
import com.ethanprentice.fitter.datamodel.data.workout.WorkoutField
import com.ethanprentice.fitter.datamodel.user.processing.UserQueryDataProcessor
import com.ethanprentice.fitter.datamodel.workout.cloud.WorkoutManagementCloudInterface
import com.ethanprentice.fitter.datamodel.workout.WorkoutManager

internal class WorkoutManagementDataProcessor(manager: Manager) : DataProcessor(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    override val cloudInterface: WorkoutManagementCloudInterface
        get() = workoutManager.managementCloudInterface

    fun createWorkout(builder: WorkoutBuilder, listener: TaskListener<Workout>) {
        if (builder.hasRequiredInputs()) {
            cloudInterface.createWorkout(builder.convertFieldsToHashMap(), object : TaskListener<String> {
                override fun onSuccess(value: String) {
                    builder.setUid(value)
                    listener.onSuccess(builder.build())
                }

                override fun onFailure(e: Exception?) {
                    listener.onFailure(e)
                }
            })
        } else {
            Log.w(TAG, "WorkoutBuilder got to WorkoutManagementDataProcessor without proper inputs!")
            listener.onFailure(null)
        }
    }

    fun updateWorkout(uid: String, fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        val strMap = HashMap<String, Any?>()
        for (key in fields.keys) {
            if (key != WorkoutField.UID) { // UID cannot be updated.

                // Format exercises
                if (key == WorkoutField.EXERCISES && (fields[WorkoutField.EXERCISES] is List<*>)) {
                    fields[WorkoutField.EXERCISES] = (fields[WorkoutField.EXERCISES] as? List<Exercise>)?.map {
                        it.convertFieldsToHashMap()
                    }
                }

                strMap[key.fieldName] = fields[key]
            }
        }
        cloudInterface.updateWorkout(uid, strMap, listener)
    }

    fun createWorkoutLog(builder: WorkoutBuilder, listener: TaskListener<String>) {
        if (builder.hasRequiredInputs() ) {
            cloudInterface.createWorkoutLog(builder.convertFieldsToHashMap(), listener)
        } else {
            Log.w(TAG, "WorkoutLogBuilder got to WorkoutManagementDataProcessor without proper inputs!")
            listener.onFailure(null)
        }
    }

    fun updateWorkoutLog(fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        val strMap = HashMap<String, Any?>()
        for (key in fields.keys) {
            strMap[key.fieldName] = fields[key]
        }
        cloudInterface.updateWorkoutLog(strMap, listener)
    }

    companion object {
        private const val TAG = "WorkoutManagementDataProcessor"
    }
}