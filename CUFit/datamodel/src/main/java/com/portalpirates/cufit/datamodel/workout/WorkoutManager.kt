package com.portalpirates.cufit.datamodel.workout

import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutCloudInterface
import com.portalpirates.cufit.datamodel.workout.processing.WorkoutDataProcessor
import com.portalpirates.cufit.datamodel.workout.provider.WorkoutProvider
import com.portalpirates.cufit.datamodel.workout.receiver.WorkoutReceiver

class WorkoutManager : Manager() {

    init {
        cloudInterfaces[GENERAL] =
            WorkoutCloudInterface(
                this
            )
        dataProcessors[GENERAL] = WorkoutDataProcessor(this)
        receivers[GENERAL] = WorkoutReceiver(this)
        providers[GENERAL] = WorkoutProvider(this)
    }

    internal fun getCloudInterface() = cloudInterfaces[GENERAL] as WorkoutCloudInterface
    internal fun getDataProcessor() = dataProcessors[GENERAL] as WorkoutDataProcessor
    internal fun getReceiver() = receivers[GENERAL] as WorkoutReceiver
    internal fun getProvider() = providers[GENERAL] as WorkoutProvider


    companion object {
        private const val GENERAL = 0 // change later to not be general when workouts are more in-depth
    }

}