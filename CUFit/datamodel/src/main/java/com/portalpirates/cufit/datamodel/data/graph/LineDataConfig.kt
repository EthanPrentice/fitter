package com.portalpirates.cufit.datamodel.data.graph



data class LineDataConfig(
        var startDate: Date,
        var endDate: Date,
        var label: String? = null,
        var movingAverageDays: Int = 1


)