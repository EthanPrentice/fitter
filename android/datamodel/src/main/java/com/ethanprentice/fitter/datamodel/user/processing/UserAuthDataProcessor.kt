package com.ethanprentice.fitter.datamodel.user.processing

import com.ethanprentice.fitter.datamodel.adt.DataProcessor
import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.ethanprentice.fitter.datamodel.user.cloud.UserAuthCloudInterface

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