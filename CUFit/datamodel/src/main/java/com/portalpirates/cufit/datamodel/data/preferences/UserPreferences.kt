package com.portalpirates.cufit.datamodel.data.preferences

class UserPreferences(var weightUnits: MeasurementUnits = MeasurementUnits.KILOGRAMS, var heightUnits: MeasurementUnits = MeasurementUnits.METERS) : Preferences() {

    fun setWeightUnitsToPounds() {
        if (this.weightUnits == MeasurementUnits.KILOGRAMS) {
            this.weightUnits = MeasurementUnits.POUNDS
        }
    }

    fun setWeightUnitsToKilograms() {
        if (this.weightUnits == MeasurementUnits.POUNDS) {
            this.weightUnits = MeasurementUnits.KILOGRAMS
        }
    }

    fun setHeightUnitsToFeet() {
        if (this.heightUnits == MeasurementUnits.METERS) {
            this.heightUnits = MeasurementUnits.FEET
        }
    }

    fun setHeightUnitsToMeters() {
        if (this.heightUnits == MeasurementUnits.FEET) {
            this.heightUnits = MeasurementUnits.METERS
        }
    }

}