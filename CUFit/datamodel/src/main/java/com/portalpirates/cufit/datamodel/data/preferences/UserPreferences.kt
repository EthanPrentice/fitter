package com.portalpirates.cufit.datamodel.data.preferences

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