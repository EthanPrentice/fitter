package com.portalpirates.cufit.datamodel.data.measure

import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.lang.IllegalArgumentException
import kotlin.math.pow
import kotlin.math.round

object MeasureConverter {

    const val TAG = "MeasureConverter"

    private val ratioMap = HashMap<Pair<MeasureUnit, MeasureUnit>, Float>()

    init {
        fun HashMap<Pair<MeasureUnit, MeasureUnit>, Float>.put(m1: MeasureUnit, m2: MeasureUnit, ratio: Float) {
            put(Pair(m1, m2), ratio)
            put(Pair(m2, m1), 1f / ratio)
        }

        /* ------ WEIGHTS ------ */

        // Weights (Metric / Imperial conversions)
        ratioMap.put(MeasureUnit.POUND, MeasureUnit.GRAM, 453.6f)
        ratioMap.put(MeasureUnit.POUND, MeasureUnit.KILOGRAM, 0.4536f)

        // Weights (Metric conversions)
        ratioMap.put(MeasureUnit.KILOGRAM, MeasureUnit.GRAM, 1000f)


        /* ------ HEIGHTS ------ */

        // Heights (Metric / Imperial conversions)
        ratioMap.put(MeasureUnit.INCH, MeasureUnit.CENTIMETER, 2.54f)
        ratioMap.put(MeasureUnit.INCH, MeasureUnit.METER, 0.0254f)
        ratioMap.put(MeasureUnit.FOOT, MeasureUnit.CENTIMETER, 30.48f)
        ratioMap.put(MeasureUnit.FOOT, MeasureUnit.METER, 0.3048f)

        // Heights (Metric conversions)
        ratioMap.put(MeasureUnit.METER, MeasureUnit.CENTIMETER, 100f)

        // Heights (Imperial conversions)
        ratioMap.put(MeasureUnit.FOOT, MeasureUnit.INCH, 12f)
    }

    /**
     * Attempts to convert [measure]'s units to [newUnit]
     * If the conversion cannot be done, throw an [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    fun convert(measure: Measure, newUnit: MeasureUnit): Measure {
        return convert(measure, newUnit, 0)
    }

    /**
     * Attempts to convert [measure]'s units to [newUnit] and round it to the nearest integer
     * If the conversion cannot be done, throw an [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    fun convert(measure: Measure, newUnit: MeasureUnit, decimals: Int): Measure {
        if (measure.unit.type != newUnit.type) {
            throw IllegalArgumentException("Cannot convert units of different types")
        }

        // MeasureUnit's equals method doesn't check real equivalence
        if (measure.unit == newUnit) {
            return if (decimals == -1) {
                 measure
            } else {
                val rounded = getRounded(measure.number, decimals)
                return Measure(rounded, measure.unit)
            }
        }

        val p = Pair(measure.unit, newUnit)
        if (ratioMap.containsKey(p)) {
            val converted = getRounded(ratioMap[p]!! * measure.number.toFloat(), decimals)
            return Measure(converted, newUnit)
        } else {
            throw IllegalArgumentException("There was an error converting ${measure.unit} to $newUnit")
        }
    }

    private fun getRounded(value: Number, decimals: Int): Double {
        if (decimals == -1) {
            return value.toDouble()
        }

        if (decimals == 0) {
            return value.toInt().toDouble()
        }

        val roundFactor = 10f.pow(decimals).toInt()
        return round(value.toDouble() * roundFactor) / roundFactor
    }
}