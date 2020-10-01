package com.portalpirates.cufit.datamodel.cloud

interface TaskListener<T> {
    fun onSuccess(value: T)
    fun onFailure(e: Exception?)
}