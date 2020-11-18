package com.portalpirates.cufit.datamodel.user.receiver

import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Receiver
import com.portalpirates.cufit.datamodel.user.UserManager
import com.portalpirates.cufit.datamodel.user.processing.UserAuthDataProcessor

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