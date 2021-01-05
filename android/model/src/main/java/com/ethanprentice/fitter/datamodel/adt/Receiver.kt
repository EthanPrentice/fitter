package com.ethanprentice.fitter.datamodel.adt

abstract class Receiver internal constructor(protected val manager: Manager) {
    internal abstract val dataProcessor: DataProcessor
}