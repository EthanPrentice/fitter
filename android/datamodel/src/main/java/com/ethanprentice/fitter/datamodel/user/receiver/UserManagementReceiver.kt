package com.ethanprentice.fitter.datamodel.user.receiver

import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.Receiver
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.user.FitUserBuilder
import com.ethanprentice.fitter.datamodel.data.user.UserField
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.ethanprentice.fitter.datamodel.user.processing.UserManagementDataProcessor

class UserManagementReceiver(manager: Manager) : Receiver(manager) {

    override val dataProcessor: UserManagementDataProcessor
        get() = (manager as UserManager).managementDataProcessor

    fun createFireStoreUser(builder: FitUserBuilder, listener: TaskListener<Unit?>) {
        dataProcessor.createFireStoreUser(builder, listener)
    }

    fun updateFireStoreUser(fields: HashMap<UserField, Any?>, listener: TaskListener<Unit?>) {
        dataProcessor.updateFireStoreUser(fields, listener)
    }

    fun updateUserEmail(email: String, listener: TaskListener<Unit?>) {
        dataProcessor.updateUserEmail(email, listener)
    }

    fun updateUserPassword(password: String, listener: TaskListener<Unit?>) {
        dataProcessor.updateUserPassword(password, listener)
    }

    fun sendVerificationEmail(listener: TaskListener<Unit?>) {
        dataProcessor.sendVerificationEmail(listener)
    }

    fun sendPasswordResetEmail(email: String, listener: TaskListener<Unit?>) {
        dataProcessor.sendPasswordResetEmail(email, listener)
    }

    fun deleteUser(listener: TaskListener<Unit?>) {
        dataProcessor.deleteUser(listener)
    }

    fun signUpUser(email: String, password: String, listener: TaskListener<Unit?>) {
        dataProcessor.signUpUser(email, password, listener)
    }

}