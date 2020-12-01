package com.portalpirates.cufit.datamodel.workout

import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutManagementCloudInterface
import com.portalpirates.cufit.datamodel.workout.cloud.WorkoutQueryCloudInterface
import com.portalpirates.cufit.datamodel.workout.provider.WorkoutProvider
import com.portalpirates.cufit.datamodel.workout.receiver.WorkoutReceiver

class WorkoutManager : Manager() {

    init {
        cloudInterfaces[QUERY] = WorkoutQueryCloudInterface(this)
        dataProcessors[QUERY] = WorkoutQueryDataProcessor(this)
        providers[QUERY] = WorkoutProvider(this)

        cloudInterfaces[MANAGEMENT] = WorkoutManagementCloudInterface(this)
        dataProcessors[MANAGEMENT] = WorkoutManagementDataProcessor(this)
        receivers[MANAGEMENT] = WorkoutReceiver(this)
    }

    /* WORKOUT QUERYING */
    internal val queryCloudInterface get() = cloudInterfaces[QUERY] as WorkoutQueryCloudInterface
    internal val queryDataProcessor  get() = dataProcessors[QUERY]  as WorkoutQueryDataProcessor
    val provider                     get() = providers[QUERY]       as WorkoutProvider

    /* WORKOUT MANAGEMENT */
    internal val managementCloudInterface get() = cloudInterfaces[MANAGEMENT] as WorkoutManagementCloudInterface
    internal val managementDataProcessor  get() = dataProcessors[MANAGEMENT]  as WorkoutManagementDataProcessor
    val receiver                          get() = receivers[MANAGEMENT]       as WorkoutReceiver

    companion object {
        private const val QUERY = 0
        private const val MANAGEMENT = 1
    }
}