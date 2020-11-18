package com.portalpirates.cufit.datamodel.workout.receiver

import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Receiver
import com.portalpirates.cufit.datamodel.workout.WorkoutManager

class WorkoutReceiver(manager: Manager) : Receiver(manager) {

    override val dataProcessor: DataProcessor
        get() = (manager as WorkoutManager).getDataProcessor()
}