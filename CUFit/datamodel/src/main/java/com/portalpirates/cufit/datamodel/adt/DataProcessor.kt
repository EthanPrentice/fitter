package com.portalpirates.cufit.datamodel.adt

internal abstract class DataProcessor(protected val manager: Manager) {
    protected abstract val cloudInterface: CloudInterface
}