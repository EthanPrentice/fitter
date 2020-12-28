package com.ethanprentice.fitter.datamodel.workout.receiver

import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.Receiver
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.workout.Workout
import com.ethanprentice.fitter.datamodel.data.workout.WorkoutBuilder
import com.ethanprentice.fitter.datamodel.data.workout.WorkoutField
import com.ethanprentice.fitter.datamodel.workout.WorkoutManager
import com.ethanprentice.fitter.datamodel.workout.processing.WorkoutManagementDataProcessor

class WorkoutReceiver(manager: Manager) : Receiver(manager) {

    override val dataProcessor: WorkoutManagementDataProcessor
        get() = (manager as WorkoutManager).managementDataProcessor

    /**
     * The task listener callback will be the UID of the newly created workout
     */
    fun createWorkout(builder: WorkoutBuilder, listener: TaskListener<Workout>) {
        dataProcessor.createWorkout(builder, listener)
    }

    fun updateWorkout(uid: String, fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        dataProcessor.updateWorkout(uid, fields, listener)
    }

    fun createWorkoutLog(builder: WorkoutBuilder, listener: TaskListener<String>) {
        dataProcessor.createWorkoutLog(builder, listener)
    }

    fun updateWorkoutLog(fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        dataProcessor.updateWorkoutLog(fields, listener)
    }
}