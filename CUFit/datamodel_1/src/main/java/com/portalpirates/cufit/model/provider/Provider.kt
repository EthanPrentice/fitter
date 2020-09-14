package com.portalpirates.cufit.model.provider

import com.portalpirates.cufit.model.processing.DataProcessor

abstract class Provider {
    // Set in the corresponding DataProcessor constructor
    private lateinit var _dataProcessor: DataProcessor
    protected val dataProcessor: DataProcessor
        get() = _dataProcessor

    fun setDataProcessor(dp: DataProcessor) {
        _dataProcessor = dp
    }
}