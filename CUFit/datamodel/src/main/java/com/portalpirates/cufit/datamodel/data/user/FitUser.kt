package com.portalpirates.cufit.datamodel.data.user

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import java.util.*

open class FitUser(
    var birthDate: Date,
    var firstName: String,
    var lastName: String,
    var currentWeight: Weight? = null,
    var currentHeight: Height? = null,
    var weightGoal: Weight? = null,
    var imageBmp: Bitmap? = null
) : SwimlaneItem {

    var uid: String? = null
        private set

    // copy constructor
    constructor(fitUser: FitUser) : this(
        fitUser.birthDate,
        fitUser.firstName,
        fitUser.lastName,
        fitUser.currentWeight,
        fitUser.currentHeight,
        fitUser.weightGoal,
        fitUser.imageBmp
    ) {
        uid = fitUser.uid
    }

    var previousWeights: MutableList<Weight> = mutableListOf()
    var previousHeights: MutableList<Height> = mutableListOf()

    val fullName: String
        get() = "$firstName $lastName"


    override fun getTitle(): String {
        return fullName
    }

    override fun getDrawable(): Drawable? {
        return BitmapDrawable(imageBmp)
    }
}