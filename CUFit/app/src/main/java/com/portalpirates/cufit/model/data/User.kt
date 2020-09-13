package com.portalpirates.cufit.model.data

import java.util.*

class User(val uid: String) {
    val birthDate: Date = Date()
    var age: Int = 0
    var firstName: String = ""
    var lastName: String = ""
    var currentWeight: Weight = Weight(0.0, MeasurementUnits.KILOGRAMS, Date())
    var weightGoal: Weight = Weight(0.0, MeasurementUnits.KILOGRAMS, Date())
    var previousWeights: List<Weight> = listOf()
}