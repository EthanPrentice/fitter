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

    private var cachedAuthenticatedUser: AuthenticatedUser? = null

    /**
     * Runs [callback] with the [FitUser]? provided by [UserDataProcessor]
     */
    fun getUserByUid(uid: String, listener: TaskListener<FitUser?>) {
        userDataProcessor.getUserByUid(uid, listener)
    }

    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>, allowCached: Boolean = true) {
        if (allowCached && cachedAuthenticatedUser != null) {
            listener.onSuccess(cachedAuthenticatedUser)
            return
        }

        userDataProcessor.getAuthenticatedUser(object : TaskListener<AuthenticatedUser?> {
            override fun onSuccess(value: AuthenticatedUser?) {
                cachedAuthenticatedUser = value
                listener.onSuccess(value)
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })
    }

    fun getFirebaseUser(): FirebaseUser? {
        return userDataProcessor.getFirebaseUser()
    }

}