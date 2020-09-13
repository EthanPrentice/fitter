package com.portalpirates.cufit.model.data

class UserPreferences(var measurementUnits: MeasurementUnits = MeasurementUnits.KILOGRAMS) : Preferences() {

    fun setMeasurementUnitsToPounds() {
        if (this.measurementUnits == MeasurementUnits.KILOGRAMS) {
            this.measurementUnits = MeasurementUnits.POUNDS
        }
    }

    fun setMeasurementUnitsToKilograms() {
        if (this.measurementUnits == MeasurementUnits.POUNDS) {
            this.measurementUnits = MeasurementUnits.KILOGRAMS
        }
    }

}