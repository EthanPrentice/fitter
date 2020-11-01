package com.portalpirates.cufit.datamodel.provider

import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.manager.Manager

class WorkoutProvider(manager: Manager) : Provider(manager) {

    fun getExploreWorkouts(): List<Workout> {
        return List(5) {
            Workout()
        }
    }

}