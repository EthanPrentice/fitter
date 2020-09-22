package com.portalpirates.cufit.datamodel.data.weight

import com.portalpirates.cufit.datamodel.data.preferences.MeasurementUnits
import java.util.*

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

    companion object {
        const val CONVERSION_FACTOR = 2.20462262
    }
}