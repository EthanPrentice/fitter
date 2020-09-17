package com.portalpirates.cufit.datamodel.cloud

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.manager.Manager

internal class UserCloudInterface(manager: Manager) : CloudInterface(manager) {

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUserByUid(uid: String, onSuccess: (doc: DocumentSnapshot?) -> Unit, onFailure: ((e: Exception) -> Unit)? = null) {
        db.collection(COLLECTION).whereEqualTo(UID, uid).limit(1).get()
            .addOnSuccessListener { result ->
                result.forEach { document ->
                    onSuccess(document)
                }
                if (result.isEmpty) {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                onFailure?.invoke(e)
            }
    }

    fun createFireStoreUser(user: HashMap<String, Any?>, callback: (success: Boolean) -> Unit) {
        val currUser = getCurrentUser()
        if (currUser == null) {
            callback(false)
        } else {
            getUserByUid(currUser.uid, { doc ->
                if (doc == null) {
                    callback(false)
                } else {
                    doc.reference.set(user)
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully created.")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error creating FireStoreUser", e)
                            callback(false)
                        }
                }
            }) {
                callback(false)
            }
        }
    }

    fun updateFireStoreUser(user: HashMap<String, Any?>, callback: (success: Boolean) -> Unit) {
        val currUser = getCurrentUser()
        if (currUser == null) {
            callback(false)
        } else {
            getUserByUid(currUser.uid, { doc ->
                if (doc == null) {
                    callback(false)
                } else {
                    doc.reference.update(user)
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully updated.")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating FireStoreUser", e)
                            callback(false)
                        }
                }
            }) {
                callback(false)
            }
        }
    }

    fun updateUserEmail(email: String, callback: (success: Boolean) -> Unit) {
        getCurrentUser()?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
                callback(task.isSuccessful)
            }
    }

    fun updateUserPassword(password: String, callback: (success: Boolean) -> Unit) {
        getCurrentUser()?.updatePassword(password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password update.")
                }
                callback(task.isSuccessful)
            }
    }

    fun sendVerificationEmail(callback: (success: Boolean) -> Unit) {
        getCurrentUser()?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email verification sent.")
                }
                callback(task.isSuccessful)
            }
    }

    fun sendPasswordResetEmail(email: String, callback: (success: Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email password reset sent.")
                }
                callback(task.isSuccessful)
            }
    }

    fun deleteUser(callback: (success: Boolean) -> Unit) {
        getCurrentUser()?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
                callback(task.isSuccessful)
            }
    }

    fun deleteFireStoreUser(callback: (success: Boolean) -> Unit) {
        val currUser = getCurrentUser()
        if (currUser == null) {
            callback(false)
        } else {
            getUserByUid(currUser.uid, { doc ->
                if (doc == null) {
                    callback(false)
                } else {
                    doc.reference.delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully deleted.")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error deleting FireStoreUser", e)
                            callback(false)
                        }
                }
            }) {
                callback(false)
            }
        }
        // TODO delete sub-collections
    }

    fun authenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Sign in with email is successful.")
                } else {
                    Log.w(TAG, "Sign in with email failed.", task.exception)
                }
                callback(task.isSuccessful)
            }
    }

    fun reAuthenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getCurrentUser()?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User re-authenticated.")
                }
                callback(task.isSuccessful)
            }
    }

    companion object {
        const val TAG = "UserCloudInterface"

        const val COLLECTION = "users"

        // Fields
        const val BIRTH_DATE = "birth_date"
        const val FIRST_NAME = "name.first"
        const val LAST_NAME = "name.last"
        const val CURRENT_WEIGHT = "current_weight"
        const val WEIGHT_GOAL = "weight_goal"
    }

}