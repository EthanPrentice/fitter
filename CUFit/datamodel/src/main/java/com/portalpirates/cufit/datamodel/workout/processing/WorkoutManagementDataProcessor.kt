package com.portalpirates.cufit.datamodel.workout.processing

import android.util.Log
import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.data.workout.WorkoutBuilder
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutManagementCloudInterface
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

internal class WorkoutManagementDataProcessor(manager: Manager) : DataProcessor(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    override val cloudInterface: WorkoutManagementCloudInterface
        get() = workoutManager.managementCloudInterface

    fun createWorkout(builder: WorkoutBuilder, listener: TaskListener<Workout>) {
        if (builder.hasRequiredInputs()) {
            cloudInterface.createWorkout(builder.convertFieldsToHashMap(), object : TaskListener<String> {
                override fun onSuccess(value: String) {
                    builder.setOwnerUid(value)
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

    companion object {
        private const val TAG = "WorkoutManagementDataProcessor"
    }
}