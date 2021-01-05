package com.ethanprentice.fitter.datamodel.user.receiver

import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.Receiver
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.ethanprentice.fitter.datamodel.user.processing.UserAuthDataProcessor

class UserAuthReceiver(manager: Manager) : Receiver(manager) {

    override val dataProcessor: UserAuthDataProcessor
        get() = (manager as UserManager).authDataProcessor

    fun authenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        dataProcessor.authenticateUser(email, password, listener)
    }

    fun reAuthenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        dataProcessor.reAuthenticateUser(email, password, listener)
    }

}