package com.ethanprentice.fitter.datamodel.data.user

import android.icu.util.MeasureUnit
import com.google.firebase.auth.FirebaseUser
import com.ethanprentice.fitter.datamodel.data.measure.Weight
import com.ethanprentice.fitter.datamodel.data.preferences.UserPreferences

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