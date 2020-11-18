package com.portalpirates.cufit.datamodel.adt

interface TaskListener<T> {
    fun onSuccess(value: T)
    fun onFailure(e: Exception?)
}