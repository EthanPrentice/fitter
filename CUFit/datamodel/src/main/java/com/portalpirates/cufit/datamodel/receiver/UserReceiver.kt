package com.portalpirates.cufit.datamodel.receiver

import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.datamodel.data.user.UserField
import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor

class UserReceiver(manager: Manager) : Receiver(manager) {

    private val userDataProcessor: UserDataProcessor
        get() = manager.dataProcessor as UserDataProcessor

    fun createFireStoreUser(builder: FitUserBuilder, callback: (success: Boolean) -> Unit) {
        userDataProcessor.createFireStoreUser(builder, callback)
    }

    fun updateFireStoreUser(fields: HashMap<UserField, Any?>, callback: (success: Boolean) -> Unit) {
        userDataProcessor.updateFireStoreUser(fields, callback)
    }

    fun updateUserEmail(email: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty()) {
            callback(false)
            return
        }
        userDataProcessor.updateUserEmail(email, callback)
    }

    fun updateUserPassword(password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (password.isEmpty()) {
            callback(false)
            return
        }
        userDataProcessor.updateUserPassword(password, callback)
    }

    fun sendVerificationEmail(callback: (success: Boolean) -> Unit) {
        userDataProcessor.sendVerificationEmail(callback)
    }

    fun sendPasswordResetEmail(email: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty()) {
            callback(false)
            return
        }
        userDataProcessor.sendPasswordResetEmail(email, callback)
    }

    fun deleteUser(callback: (success: Boolean) -> Unit) {
        userDataProcessor.deleteUser(callback)
    }

    fun authenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
            return
        }
        userDataProcessor.authenticateUser(email, password, callback)
    }

    fun reAuthenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
            return
        }
        userDataProcessor.reAuthenticateUser(email, password, callback)
    }

}