package com.ethanprentice.fitter.datamodel.adt

interface TaskListener<T> {
    fun onSuccess(value: T)
    fun onFailure(e: Exception?)
}