package com.portalpirates.cufit.datamodel.workout.provider

import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Provider
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

class WorkoutProvider(manager: Manager) : Provider(manager) {

    override val dataProcessor: DataProcessor
        get() = (manager as WorkoutManager).getDataProcessor()

    fun getExploreWorkouts(): List<Workout> {
        return List(5) {
            Workout()
        }
    }

}