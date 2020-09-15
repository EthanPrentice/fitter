package com.portalpirates.cufit.datamodel.receiver

import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor

class UserReceiver(manager: Manager) : Receiver(manager) {

    private val dataProcessor: UserDataProcessor
        get() = manager.dataProcessor as UserDataProcessor

    fun authenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
            return
        }
        dataProcessor.authenticateUser(email, password, callback)
    }
}