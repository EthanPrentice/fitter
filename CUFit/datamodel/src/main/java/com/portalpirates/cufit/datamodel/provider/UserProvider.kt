package com.portalpirates.cufit.datamodel.provider

import com.google.firebase.auth.FirebaseUser
import com.portalpirates.cufit.datamodel.cloud.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor

class UserProvider(manager: Manager) : Provider(manager) {

    private val userDataProcessor: UserDataProcessor
        get() = manager.dataProcessor as UserDataProcessor

    fun getUserByUid(uid: String, listener: TaskListener<FitUser?>) {
        userDataProcessor.getUserByUid(uid, listener)
    }

    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>) {
        userDataProcessor.getAuthenticatedUser(listener)
    }

    fun getFirebaseUser(): FirebaseUser? {
        return userDataProcessor.getFirebaseUser()
    }

    fun userFinishedWelcomeFlow(listener: TaskListener<Boolean>) {
        userDataProcessor.userFinishedWelcomeFlow(listener)
    }

}