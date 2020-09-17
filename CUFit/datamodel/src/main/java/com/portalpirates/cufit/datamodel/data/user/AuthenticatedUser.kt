package com.portalpirates.cufit.datamodel.data.user

import com.google.firebase.auth.FirebaseUser
import com.portalpirates.cufit.datamodel.data.weight.Weight
import java.util.*

class AuthenticatedUser(val fbUser: FirebaseUser, birthDate: Date, firstName: String, lastName: String) : FitUser(
    birthDate, firstName, lastName
) {
    constructor(fbUser: FirebaseUser, fitUser: FitUser) : this(
        fbUser, fitUser.birthDate, fitUser.firstName, fitUser.lastName
    )


    fun addPreviousWeight(weight: Weight) {
        // TODO: add these to FireStore as well
        previousWeights.add(weight)
    }

}