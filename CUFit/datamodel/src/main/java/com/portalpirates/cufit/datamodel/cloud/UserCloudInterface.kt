package com.portalpirates.cufit.datamodel.cloud

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.portalpirates.cufit.datamodel.manager.Manager

internal class UserCloudInterface(manager: Manager) : CloudInterface(manager) {

    fun getFirebaseUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUserByUid(uid: String, onSuccess: (doc: DocumentSnapshot?) -> Unit, onFailure: ((e: Exception) -> Unit)? = null) {
        db.collection(COLLECTION).document(uid).get()
            .addOnSuccessListener { result ->
                onSuccess(result)
            }
            .addOnFailureListener { e ->
                onFailure?.invoke(e)
            }
    }

    /**
     * Creates a user in FireStore.  This user has already been created from a server-side script after an auth user is created
     * so we just populate it here
     */
    fun createFireStoreUser(fields: HashMap<String, Any?>, callback: (success: Boolean) -> Unit) {
        val currUser = getFirebaseUser()
        if (currUser == null) {
            callback(false)
        } else {
            getUserByUid(currUser.uid, { doc ->
                if (doc == null) {
                    callback(false)
                } else {
                    doc.reference.update(fields)
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

    /**
     * Updates the currently authenticated user's fields with the entries in [fields]
     */
    fun updateFireStoreUser(fields: HashMap<String, Any?>, callback: (success: Boolean) -> Unit) {
        val currUser = getFirebaseUser()
        if (currUser == null) {
            callback(false)
        } else {
            getUserByUid(currUser.uid, { doc ->
                if (doc == null) {
                    callback(false)
                } else {
                    doc.reference.update(fields)
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

    /**
     * Updates the currently authenticated user's email address
     */
    fun updateUserEmail(email: String, callback: (success: Boolean) -> Unit) {
        getFirebaseUser()?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
                callback(task.isSuccessful)
            }
    }

    /**
     * Updates the currently authenticated user's password
     */
    fun updateUserPassword(password: String, callback: (success: Boolean) -> Unit) {
        getFirebaseUser()?.updatePassword(password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password update.")
                }
                callback(task.isSuccessful)
            }
    }

    fun sendVerificationEmail(callback: (success: Boolean) -> Unit) {
        getFirebaseUser()?.sendEmailVerification()
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

    /**
     * Deletes the currently authenticated user from auth and FireStore
     */
    fun deleteUser(callback: (success: Boolean) -> Unit) {
        getFirebaseUser()?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                    deleteFireStoreUser(callback)
                }
            }
    }

    /**
     * Deletes the currently authenticated user from FireStore
     * Should only be called when also deleting user from authentication
     */
    private fun deleteFireStoreUser(callback: (success: Boolean) -> Unit) {
        val currUser = getFirebaseUser()
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

    /**
     * Signs the user up, adding them to the authentication server
     * Then a server-side script with create a FireStore user for us to populate later
     */
    fun signUpUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Create user with email is successful.")
                } else {
                    Log.w(TAG, "Create user with email failed.", task.exception)
                }
                callback(task.isSuccessful)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, e.message ?: "Error signing up a new user")
            }
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
        getFirebaseUser()?.reauthenticate(credential)
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
        const val SEX = "gender"
    }

}