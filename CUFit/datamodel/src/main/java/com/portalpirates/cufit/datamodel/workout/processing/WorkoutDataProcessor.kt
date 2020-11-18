package com.portalpirates.cufit.datamodel.workout.processing

import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutCloudInterface
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

internal class WorkoutDataProcessor(manager: Manager) : DataProcessor(manager) {
    override val cloudInterface: WorkoutCloudInterface
        get() = (manager as WorkoutManager).getCloudInterface()
}