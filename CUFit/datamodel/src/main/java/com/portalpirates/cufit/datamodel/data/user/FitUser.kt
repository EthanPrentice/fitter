package com.portalpirates.cufit.datamodel.data.user

import com.portalpirates.cufit.datamodel.data.weight.Weight
import java.util.*

open class FitUser(var birthDate: Date, var firstName: String, var lastName: String, var currentWeight: Weight? = null, var weightGoal: Weight? = null) {

    var previousWeights: MutableList<Weight> = mutableListOf()

    val fullName: String
        get() = "$firstName $lastName"

    fun addPreviousWeight(weight: Weight) {
        previousWeights.add(weight)
    }

}