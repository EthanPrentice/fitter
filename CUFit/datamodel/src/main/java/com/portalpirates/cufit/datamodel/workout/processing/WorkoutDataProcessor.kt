package com.portalpirates.cufit.datamodel.workout.processing

import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutManagementCloudInterface
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

internal class WorkoutDataProcessor(manager: Manager) : DataProcessor(manager) {
    override val cloudInterface: WorkoutManagementCloudInterface
        get() = (manager as WorkoutManager).getCloudInterface()
}