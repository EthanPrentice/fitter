package com.portalpirates.cufit.datamodel.adt

abstract class Manager protected constructor() {
    internal val cloudInterfaces = HashMap<Int, CloudInterface>()
    internal val dataProcessors = HashMap<Int, DataProcessor>()
    protected val providers = HashMap<Int, Provider>()
    protected val receivers = HashMap<Int, Receiver>()
}