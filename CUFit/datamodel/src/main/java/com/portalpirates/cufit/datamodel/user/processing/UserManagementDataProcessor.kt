package com.portalpirates.cufit.datamodel.user.processing

import android.util.Log
import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.datamodel.data.user.UserField
import com.portalpirates.cufit.datamodel.user.UserManager
import com.portalpirates.cufit.datamodel.user.cloud.UserManagementCloudInterface

internal class UserManagementDataProcessor(manager: Manager) : DataProcessor(manager) {

    private val userManager: UserManager
        get() = manager as UserManager

    override val cloudInterface: UserManagementCloudInterface
        get() = userManager.managementCloudInterface


    fun createFireStoreUser(builder: FitUserBuilder, listener: TaskListener<Unit?>) {
        if (builder.hasRequiredInputs()) {
            cloudInterface.createFireStoreUser(builder.convertFieldsToHashMap(), listener)
        } else {
            Log.w(TAG, "FitUserBuilder got to UserDataProcessor without proper inputs!")
            listener.onFailure(null)
        }
    }

    fun updateFireStoreUser(fields: HashMap<UserField, Any?>, listener: TaskListener<Unit?>) {
        val strMap = HashMap<String, Any?>()
        for (key in fields.keys) {
            strMap[key.fieldName] = fields[key]
        }
        cloudInterface.updateFireStoreUser(strMap, listener)
    }

    fun updateUserEmail(email: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (email.isEmpty()) {
            listener.onFailure(null)
            return
        }

        cloudInterface.updateUserEmail(email, listener)
    }

    fun updateUserPassword(password: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (password.isEmpty()) {
            listener.onFailure(null)
            return
        }

        cloudInterface.updateUserPassword(password, listener)
    }

    fun sendVerificationEmail(listener: TaskListener<Unit?>) {
        cloudInterface.sendVerificationEmail(listener)
    }

    fun sendPasswordResetEmail(email: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (email.isEmpty()) {
            listener.onFailure(null)
            return
        }

        cloudInterface.sendPasswordResetEmail(email, listener)
    }

    fun deleteUser(listener: TaskListener<Unit?>) {
        // Ask cloud if they'd rather us delete the fireStore or fireBase user, then the other
        // could be deleted automatically on cloud
        cloudInterface.deleteUser(listener)
    }

    /**
     * Sign Up user
     * TODO: if we're doing input validation on the client, add that here
     * Such as verifying that the new account's password was correctly typed and meets the complexity reqs
     */
    fun signUpUser(email: String, password: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            listener.onFailure(null)
            return
        }

        cloudInterface.signUpUser(email, password, listener)
    }

    companion object {
        private const val TAG = "UserManagementDataProcessor"
    }
}