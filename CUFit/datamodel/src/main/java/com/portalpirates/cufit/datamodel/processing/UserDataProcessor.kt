package com.portalpirates.cufit.datamodel.processing

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface.Companion.BIRTH_DATE
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface.Companion.FIRST_NAME
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface.Companion.LAST_NAME
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.datamodel.data.user.UserField
import com.portalpirates.cufit.datamodel.manager.Manager
import java.lang.Exception

internal class UserDataProcessor(manager: Manager) : DataProcessor(manager) {

    // Helper function for casting
    private val userCloudInterface: UserCloudInterface
        get() = cloudInterface as UserCloudInterface


    fun getAuthenticatedUser(callback: (AuthenticatedUser?) -> Unit) {
        val firebaseUser = userCloudInterface.getFirebaseUser()
        if (firebaseUser == null) {
            callback(null)
        } else {
            getUserByUid(firebaseUser.uid) { fitUser ->
                if (fitUser != null) {
                    val authenticatedUser = AuthenticatedUser(firebaseUser, fitUser)
                    callback(authenticatedUser)
                } else {
                    callback(null)
                }
            }
        }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return userCloudInterface.getFirebaseUser()
    }

    /**
     * Runs [callback] with the [FitUser] created from the document provided by the [UserCloudInterface]
     * If no User can be created, run the callback with null
     */
    fun getUserByUid(uid: String, callback: (FitUser?) -> Unit) {
        userCloudInterface.getUserByUid(uid, callback@{ doc ->
            // If a user is found by the CloudInterface, create a User from the document and call the onSuccessListener
            if (doc != null) {
                val user = createUserFromDocument(doc)
                if (user != null) {
                    callback(user)
                    return@callback
                }
            }
            // If a user is not found, or one could not be created from the supplied document, run onSuccess with null
            callback(null)
        })
    }

    fun createFireStoreUser(builder: FitUserBuilder, callback: (success: Boolean) -> Unit) {
        userCloudInterface.createFireStoreUser(builder.convertFieldsToHashMap(), callback)
    }

    fun updateFireStoreUser(fields: HashMap<UserField, Any?>, callback: (success: Boolean) -> Unit) {
        val strMap = HashMap<String, Any?>()
        for (key in fields.keys) {
            strMap[key.fieldName] = fields[key]
        }
        userCloudInterface.updateFireStoreUser(strMap, callback)
    }

    fun updateUserEmail(email: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty()) {
            callback(false)
            return
        }

        userCloudInterface.updateUserEmail(email, callback)
    }

    fun updateUserPassword(password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (password.isEmpty()) {
            callback(false)
            return
        }

        userCloudInterface.updateUserPassword(password, callback)
    }

    fun sendVerificationEmail(callback: (success: Boolean) -> Unit) {
        userCloudInterface.sendVerificationEmail(callback)
    }

    fun sendPasswordResetEmail(email: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty()) {
            callback(false)
            return
        }

        userCloudInterface.sendPasswordResetEmail(email, callback)
    }

    fun deleteUser(callback: (success: Boolean) -> Unit) {
        // Ask cloud if they'd rather us delete the fireStore or fireBase user, then the other
        // could be deleted automatically on cloud
        userCloudInterface.deleteUser(callback)
    }

    /**
     * Sign Up user
     * TODO: if we're doing input validation on the client, add that here
     * Such as verifying that the new account's password was correctly typed and meets the complexity reqs
     */
    fun signUpUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
            return
        }

        userCloudInterface.signUpUser(email, password, callback)
    }

    /**
     * Authenticate user
     * TODO: if we're doing input validation on the client, add that here
     */
    fun authenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
            return
        }

        userCloudInterface.authenticateUser(email, password, callback)
    }

    fun reAuthenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
            return
        }

        userCloudInterface.reAuthenticateUser(email, password, callback)
    }

    private fun createUserFromDocument(doc: DocumentSnapshot) : FitUser? {
        return try {
            FitUser(
                doc.getDate(BIRTH_DATE)!!,
                doc.getString(FIRST_NAME)!!,
                doc.getString(LAST_NAME)!!
            )
        } catch(e: Exception) {
            Log.e(TAG, "Could not create a user from document")
            null
        }
    }

    companion object {
        const val TAG = "UserDataProcessor"
    }

}