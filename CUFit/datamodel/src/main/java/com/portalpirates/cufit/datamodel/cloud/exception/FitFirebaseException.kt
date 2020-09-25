package com.portalpirates.cufit.datamodel.cloud.exception

import com.google.firebase.FirebaseException

/**
 * Note that this does not inherit from [FitException]
 * We should be using this to handle cloud error messages, possibly using it to filter them in the future
 * and then show them to the user
 */
class FitFirebaseException : FirebaseException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable?) : super(message, cause)
}