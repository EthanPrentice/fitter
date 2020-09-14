package com.portalpirates.cufit.datamodel.data.user

import java.util.*

open class FitUser(var birthDate: Date, var firstName: String, var lastName: String) {

    val fullName: String
        get() = "$firstName $lastName"

}