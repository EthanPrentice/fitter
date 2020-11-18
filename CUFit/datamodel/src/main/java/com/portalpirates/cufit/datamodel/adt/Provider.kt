package com.portalpirates.cufit.datamodel.adt

abstract class Provider internal constructor(protected val manager: Manager) {
    internal abstract val dataProcessor: DataProcessor
}