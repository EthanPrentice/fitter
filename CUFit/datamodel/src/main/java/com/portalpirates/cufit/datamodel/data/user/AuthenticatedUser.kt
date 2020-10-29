package com.portalpirates.cufit.datamodel.data.user

import android.icu.util.MeasureUnit
import com.google.firebase.auth.FirebaseUser
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.preferences.UserPreferences
import java.util.*

class AuthenticatedUser(val fbUser: FirebaseUser, fitUser: FitUser) : FitUser(fitUser) {


    fun addPreviousWeight(weight: Weight) {
        // TODO: add these to FireStore as well
        previousWeights.add(weight)
    }

    fun getPreferences(): UserPreferences {
        // just return a default until we implement in the cloud
        return UserPreferences(MeasureUnit.POUND, MeasureUnit.CENTIMETER)
    }

}