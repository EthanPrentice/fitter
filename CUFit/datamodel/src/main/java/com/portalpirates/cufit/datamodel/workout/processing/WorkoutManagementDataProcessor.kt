package com.portalpirates.cufit.datamodel.workout.processing

import android.util.Log
import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.WorkoutBuilder
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutManagementCloudInterface
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

internal class WorkoutManagementDataProcessor(manager: Manager) : DataProcessor(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    override val cloudInterface: WorkoutManagementCloudInterface
        get() = workoutManager.managementCloudInterface

    fun createWorkout(builder: WorkoutBuilder, listener: TaskListener<Unit?>) {
        if (builder.hasRequiredInputs()) {
            cloudInterface.createWorkout(builder.convertFieldsToHashMap(), listener)
        } else {
            Log.w(TAG, "WorkoutBuilder got to WorkoutManagementDataProcessor without proper inputs!")
            listener.onFailure(null)
        }
    }

    fun updateWorkout(fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        val strMap = HashMap<String, Any?>()
        for (key in fields.keys) {
            strMap[key.fieldName] = fields[key]
        }
        cloudInterface.updateWorkout(strMap, listener)
    }

    companion object {
        private const val TAG = "WorkoutManagementDataProcessor"
    }
}