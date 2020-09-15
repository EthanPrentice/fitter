package com.portalpirates.cufit.datamodel.data.user

import com.google.firebase.auth.FirebaseUser
import java.util.*

class AuthenticatedUser(val fbUser: FirebaseUser, birthDate: Date, firstName: String, lastName: String) : FitUser(
    birthDate, firstName, lastName
) {
    constructor(fbUser: FirebaseUser, fitUser: FitUser) : this(
        fbUser, fitUser.birthDate, fitUser.firstName, fitUser.lastName
    )

}