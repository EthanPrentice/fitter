package com.portalpirates.cufit.datamodel.data.measure

import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.util.*

abstract class FitMeasure(number: Number, unit: MeasureUnit, val dateLogged: Date) : Measure(number, unit) {



}