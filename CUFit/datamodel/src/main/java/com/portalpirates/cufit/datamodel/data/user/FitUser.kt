package com.portalpirates.cufit.datamodel.data.user

import android.graphics.Bitmap
import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.measure.Weight
import java.util.*

open class FitUser(
    var birthDate: Date,
    var firstName: String,
    var lastName: String,
    var currentWeight: Weight? = null,
    var currentHeight: Height? = null,
    var weightGoal: Weight? = null,
    var imageBmp: Bitmap? = null
) {
    // copy constructor
    constructor(fitUser: FitUser) : this(
        fitUser.birthDate,
        fitUser.firstName,
        fitUser.lastName,
        fitUser.currentWeight,
        fitUser.currentHeight,
        fitUser.weightGoal,
        fitUser.imageBmp
    )

    var previousWeights: MutableList<Weight> = mutableListOf()
    var previousHeights: MutableList<Height> = mutableListOf()

    val fullName: String
        get() = "$firstName $lastName"

}