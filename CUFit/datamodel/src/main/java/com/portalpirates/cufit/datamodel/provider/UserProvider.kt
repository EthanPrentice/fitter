package com.portalpirates.cufit.datamodel.provider

import com.google.firebase.auth.FirebaseUser
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor

class UserProvider(manager: Manager) : Provider(manager) {

    private val userDataProcessor: UserDataProcessor
        get() = manager.dataProcessor as UserDataProcessor

    /**
     * Runs [callback] with the [FitUser]? provided by [UserDataProcessor]
     */
    fun getUserByUid(uid: String, callback: (user: FitUser?) -> Unit) {
        userDataProcessor.getUserByUid(uid, callback)
    }

    fun getCurrentUser(callback: (AuthenticatedUser?) -> Unit) {
        userDataProcessor.getCurrentUser(callback)
    }

    fun getFirebaseUser(): FirebaseUser? {
        return userDataProcessor.getFirebaseUser()
    }

}