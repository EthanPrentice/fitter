package com.portalpirates.cufit.datamodel.receiver

import com.portalpirates.cufit.datamodel.cloud.TaskListener
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.datamodel.data.user.UserField
import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor

class UserReceiver(manager: Manager) : Receiver(manager) {

    private val userDataProcessor: UserDataProcessor
        get() = manager.dataProcessor as UserDataProcessor

    fun createFireStoreUser(builder: FitUserBuilder, listener: TaskListener<Unit?>) {
        userDataProcessor.createFireStoreUser(builder, listener)
    }

    fun updateFireStoreUser(fields: HashMap<UserField, Any?>, listener: TaskListener<Unit?>) {
        userDataProcessor.updateFireStoreUser(fields, listener)
    }

    fun updateUserEmail(email: String, listener: TaskListener<Unit?>) {
        userDataProcessor.updateUserEmail(email, listener)
    }

    fun updateUserPassword(password: String, listener: TaskListener<Unit?>) {
        userDataProcessor.updateUserPassword(password, listener)
    }

    fun sendVerificationEmail(listener: TaskListener<Unit?>) {
        userDataProcessor.sendVerificationEmail(listener)
    }

    fun sendPasswordResetEmail(email: String, listener: TaskListener<Unit?>) {
        userDataProcessor.sendPasswordResetEmail(email, listener)
    }

    fun deleteUser(listener: TaskListener<Unit?>) {
        userDataProcessor.deleteUser(listener)
    }

    fun signUpUser(email: String, password: String, listener: TaskListener<Unit?>) {
        userDataProcessor.signUpUser(email, password, listener)
    }

    fun authenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        userDataProcessor.authenticateUser(email, password, listener)
    }

    fun reAuthenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        userDataProcessor.reAuthenticateUser(email, password, listener)
    }

}