package com.portalpirates.cufit.datamodel.manager

import com.portalpirates.cufit.datamodel.cloud.WorkoutCloudInterface
import com.portalpirates.cufit.datamodel.processing.WorkoutDataProcessor
import com.portalpirates.cufit.datamodel.provider.WorkoutProvider
import com.portalpirates.cufit.datamodel.receiver.WorkoutReceiver

class WorkoutManager : Manager() {
    override val cloudInterface = WorkoutCloudInterface(this)
    override val dataProcessor = WorkoutDataProcessor(this)
    override val provider = WorkoutProvider(this)
    override val receiver = WorkoutReceiver(this)
}