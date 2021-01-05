package com.ethanprentice.fitter.datamodel.adt

abstract class Manager internal constructor() {
    internal val cloudInterfaces = HashMap<Int, CloudInterface>()
    internal val dataProcessors = HashMap<Int, DataProcessor>()
    protected val providers = HashMap<Int, Provider>()
    protected val receivers = HashMap<Int, Receiver>()
}