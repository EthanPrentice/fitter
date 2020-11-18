package com.portalpirates.cufit.datamodel


/**
 * Base class of exception to be thrown client-side for errors that are not thrown by Firebase
 * If throwing a FirebaseException and you want more control over it, subclass FitFirebaseException instead, or make a new instance of the base class
 */
abstract class FitException(message: String, throwable: Throwable?) : Exception(message, throwable) {
    constructor(message: String) : this(message, null)
}