package com.ethanprentice.fitter.datamodel.adt

internal abstract class DataProcessor(protected val manager: Manager) {
    protected abstract val cloudInterface: CloudInterface
}