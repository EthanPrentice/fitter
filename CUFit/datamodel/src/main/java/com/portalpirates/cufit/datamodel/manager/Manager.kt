package com.portalpirates.cufit.datamodel.manager

import com.portalpirates.cufit.datamodel.cloud.CloudInterface
import com.portalpirates.cufit.datamodel.processing.DataProcessor
import com.portalpirates.cufit.datamodel.provider.Provider
import com.portalpirates.cufit.datamodel.receiver.Receiver

abstract class Manager internal constructor() {
    internal abstract val cloudInterface: CloudInterface
    internal abstract val dataProcessor: DataProcessor
    abstract val provider: Provider
    abstract val receiver: Receiver
}