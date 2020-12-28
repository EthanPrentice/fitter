package com.ethanprentice.fitter.datamodel.data.measure

import android.icu.util.MeasureUnit

enum class CloudUnit(val measureUnit: MeasureUnit, val cloudId: String) {
    KILOGRAM(MeasureUnit.KILOGRAM, "kg"),
    POUND(MeasureUnit.POUND, "lb"),

    INCH(MeasureUnit.INCH, "inch"),
    CENTIMETER(MeasureUnit.CENTIMETER, "cm");

    companion object {
        fun getFromCloudId(id: String): CloudUnit? {
            for (v in values()) {
                if (v.cloudId == id) {
                    return v
                }
            }
            return null
        }

        fun getFromMeasureUnit(unit: MeasureUnit): CloudUnit? {
            for (v in values()) {
                if (v.measureUnit == unit) {
                    return v
                }
            }
            return null
        }
    }
}