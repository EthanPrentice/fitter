package com.portalpirates.cufit.datamodel.processing

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.cloud.TaskListener
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface.Companion.BIRTH_DATE
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface.Companion.FIRST_NAME
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface.Companion.LAST_NAME
import com.portalpirates.cufit.datamodel.cloud.exception.UnauthorizedUserException
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.datamodel.data.user.UserField
import com.portalpirates.cufit.datamodel.manager.Manager
import kotlin.Exception

internal class UserDataProcessor(manager: Manager) : DataProcessor(manager) {

    // Helper function for casting
    private val userCloudInterface: UserCloudInterface
        get() = cloudInterface as UserCloudInterface


    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>) {
        val firebaseUser = userCloudInterface.getFirebaseUser()
        if (firebaseUser == null) {
            listener.onFailure(UnauthorizedUserException())
        } else {
            getUserByUid(firebaseUser.uid, object : TaskListener<FitUser?> {
                override fun onSuccess(value: FitUser?) {
                    if (value != null) {
                        val authenticatedUser = AuthenticatedUser(firebaseUser, value)
                        listener.onSuccess(authenticatedUser)
                    } else {
                        listener.onFailure(null)
                    }
                }
                override fun onFailure(e: Exception?) = listener.onFailure(e)
            })
        }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return userCloudInterface.getFirebaseUser()
    }

    /**
     * Runs [listener] with the [FitUser] created from the document provided by the [UserCloudInterface]
     * If no User can be created, run the listener with null
     */
    fun getUserByUid(uid: String, listener: TaskListener<FitUser?>) {
        userCloudInterface.getUserByUid(uid, object : TaskListener<DocumentSnapshot> {
            // If a user is found by the CloudInterface, create a User from the document and call the onSuccessListener
            override fun onSuccess(value: DocumentSnapshot) {
                val user = createUserFromDocument(value)
                listener.onSuccess(user)
            }
            // If a user is not found, or one could not be created from the supplied document, run onSuccess with null
            override fun onFailure(e: Exception?) = listener.onFailure(e)
        })
    }

    fun createFireStoreUser(builder: FitUserBuilder, listener: TaskListener<Unit?>) {
        if (builder.hasRequiredInputs()) {
            userCloudInterface.createFireStoreUser(builder.convertFieldsToHashMap(), listener)
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
        userCloudInterface.updateFireStoreUser(strMap, listener)
    }

    fun updateUserEmail(email: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (email.isEmpty()) {
            listener.onFailure(null)
            return
        }

        userCloudInterface.updateUserEmail(email, listener)
    }

    fun updateUserPassword(password: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (password.isEmpty()) {
            listener.onFailure(null)
            return
        }

        userCloudInterface.updateUserPassword(password, listener)
    }

    fun sendVerificationEmail(listener: TaskListener<Unit?>) {
        userCloudInterface.sendVerificationEmail(listener)
    }

    fun sendPasswordResetEmail(email: String, listener: TaskListener<Unit?>) {
        // Must be non-empty
        if (email.isEmpty()) {
            listener.onFailure(null)
            return
        }

        userCloudInterface.sendPasswordResetEmail(email, listener)
    }

    fun deleteUser(listener: TaskListener<Unit?>) {
        // Ask cloud if they'd rather us delete the fireStore or fireBase user, then the other
        // could be deleted automatically on cloud
        userCloudInterface.deleteUser(listener)
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

        userCloudInterface.signUpUser(email, password, listener)
    }

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

        userCloudInterface.authenticateUser(email, password, listener)
    }

    fun reAuthenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {        // Must be non-empty
        if (email.isEmpty() || password.isEmpty()) {
            listener.onFailure(null)
            return
        }

        userCloudInterface.reAuthenticateUser(email, password, listener)
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