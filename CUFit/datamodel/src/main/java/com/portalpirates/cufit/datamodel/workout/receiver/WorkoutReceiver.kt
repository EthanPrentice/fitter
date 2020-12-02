package com.portalpirates.cufit.datamodel.workout.receiver

import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Receiver
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.WorkoutBuilder
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.datamodel.workout.WorkoutManager
import com.portalpirates.cufit.datamodel.workout.processing.WorkoutManagementDataProcessor

class WorkoutReceiver(manager: Manager) : Receiver(manager) {

    override val dataProcessor: WorkoutManagementDataProcessor
        get() = (manager as WorkoutManager).managementDataProcessor

    /**
     * The task listener callback will be the UID of the newly created workout
     */
    fun createWorkout(builder: WorkoutBuilder, listener: TaskListener<String>) {
        dataProcessor.createWorkout(builder, listener)
    }

    fun updateWorkout(fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        dataProcessor.updateWorkout(fields, listener)
    }
}