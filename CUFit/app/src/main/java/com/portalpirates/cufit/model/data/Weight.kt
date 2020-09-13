package com.portalpirates.cufit.model.data

import java.util.*

const val CONVERSION_FACTOR = 2.20462262

class Weight(var mass: Double, var measurementUnits: MeasurementUnits = MeasurementUnits.KILOGRAMS, val dateLogged : Date) {

    fun convertToPounds() {
        if (this.measurementUnits == MeasurementUnits.KILOGRAMS) {
            this.mass *= CONVERSION_FACTOR
            this.measurementUnits = MeasurementUnits.POUNDS
        }
    }

    fun convertToKilograms() {
        if (this.measurementUnits == MeasurementUnits.POUNDS) {
            this.mass /= CONVERSION_FACTOR
            this.measurementUnits = MeasurementUnits.KILOGRAMS
        }
    }

}