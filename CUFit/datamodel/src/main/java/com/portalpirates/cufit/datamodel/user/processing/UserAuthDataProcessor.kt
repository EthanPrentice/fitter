package com.portalpirates.cufit.datamodel.user.processing

import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.user.UserManager
import com.portalpirates.cufit.datamodel.user.cloud.UserAuthCloudInterface

internal class UserAuthDataProcessor(manager: Manager) : DataProcessor(manager) {

    override val cloudInterface: UserAuthCloudInterface
        get() = (manager as UserManager).authCloudInterface

    /**
     * Authenticate user
     * TODO: if we're doing input validation on the client, add that here
     */
    fun authenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            listener.onFailure(null)
            return
        }

        cloudInterface.authenticateUser(email, password, listener)
    }

    fun reAuthenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            listener.onFailure(null)
            return
        }

        cloudInterface.reAuthenticateUser(email, password, listener)
    }
}