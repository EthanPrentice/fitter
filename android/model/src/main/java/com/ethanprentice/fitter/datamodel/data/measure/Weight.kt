package com.ethanprentice.fitter.datamodel.data.measure

import android.icu.util.MeasureUnit
import java.util.*

class Weight(number: Number, unit: MeasureUnit?, dateLogged : Date) : FitMeasure(number, unit ?: BASE_UNIT, dateLogged) {
    constructor(number: Number, dateLogged : Date) : this (number, BASE_UNIT, dateLogged)

    companion object {
        val BASE_UNIT: MeasureUnit = MeasureUnit.GRAM

        const val type = "mass"
    }
}