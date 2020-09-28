package com.portalpirates.cufit.datamodel.data.user

import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.measure.Weight
import java.util.*

open class FitUser(
    var birthDate: Date,
    var firstName: String,
    var lastName: String,
    var currentWeight: Weight? = null,
    var currentHeight: Height? = null,
    var weightGoal: Weight? = null
) {

    var previousWeights: MutableList<Weight> = mutableListOf()
    var previousHeights: MutableList<Height> = mutableListOf()

    val fullName: String
        get() = "$firstName $lastName"

}