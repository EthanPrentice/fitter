package com.ethanprentice.fitter.datamodel.workout

import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.workout.cloud.WorkoutManagementCloudInterface
import com.ethanprentice.fitter.datamodel.workout.cloud.WorkoutQueryCloudInterface
import com.ethanprentice.fitter.datamodel.workout.processing.WorkoutManagementDataProcessor
import com.ethanprentice.fitter.datamodel.workout.processing.WorkoutQueryDataProcessor
import com.ethanprentice.fitter.datamodel.workout.provider.WorkoutProvider
import com.ethanprentice.fitter.datamodel.workout.receiver.WorkoutReceiver

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