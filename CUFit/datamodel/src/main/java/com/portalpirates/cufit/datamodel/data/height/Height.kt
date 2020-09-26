package com.portalpirates.cufit.datamodel.data.height

import com.portalpirates.cufit.datamodel.data.preferences.MeasurementUnits
import java.util.*

class Height(var length: Double, var measurementUnits: MeasurementUnits = MeasurementUnits.METERS, val dateLogged : Date) {

    fun convertToFeet() {
        if (this.measurementUnits == MeasurementUnits.METERS) {
            this.length *= CONVERSION_FACTOR
            this.measurementUnits = MeasurementUnits.FEET
        }
    }

    fun convertToMeters() {
        if (this.measurementUnits == MeasurementUnits.FEET) {
            this.length /= CONVERSION_FACTOR
            this.measurementUnits = MeasurementUnits.METERS
        }
    }

    companion object {
        const val CONVERSION_FACTOR = 3.2808399
    }
}