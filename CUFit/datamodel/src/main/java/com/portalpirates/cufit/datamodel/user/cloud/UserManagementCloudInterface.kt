package com.portalpirates.cufit.datamodel.user.cloud

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.CloudInterface
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.exception.UnauthorizedUserException
import com.portalpirates.cufit.datamodel.user.UserManager

internal class UserManagementCloudInterface(manager: Manager) : CloudInterface(manager) {

    private val userManager: UserManager
        get() = manager as UserManager


    /**
     * Creates a user in FireStore.  This user has already been created from a server-side script after an auth user is created
     * so we just populate it here
     */
    fun createFireStoreUser(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        val currUser = userManager.queryCloudInterface.getFirebaseUser()
        if (currUser == null) {
            listener.onFailure(UnauthorizedUserException())
        } else {
            userManager.queryCloudInterface.getUserByUid(currUser.uid, object :
                TaskListener<DocumentSnapshot> {
                override fun onSuccess(value: DocumentSnapshot) {
                    value.reference.update(fields)
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully created.")
                            listener.onSuccess(null)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error creating FireStoreUser", e)
                            listener.onFailure(e)
                        }
                }

                override fun onFailure(e: Exception?) {
                    listener.onFailure(e)
                }
            })
        }
    }

    /**
     * Updates the currently authenticated user's fields with the entries in [fields]
     */
    fun updateFireStoreUser(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        val currUser = userManager.queryCloudInterface.getFirebaseUser()
        if (currUser == null) {
            listener.onFailure(null)
        } else {
            userManager.queryCloudInterface.getUserByUid(currUser.uid, object :
                TaskListener<DocumentSnapshot> {
                override fun onSuccess(value: DocumentSnapshot) {
                    value.reference.update(fields)
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully updated.")
                            listener.onSuccess(null)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating FireStoreUser", e)
                            listener.onFailure(e)
                        }
                }

                override fun onFailure(e: Exception?) {
                    listener.onFailure(e)
                }
            })
        }
    }

    /**
     * Updates the currently authenticated user's email address
     */
    fun updateUserEmail(email: String, listener: TaskListener<Unit?>) {
        userManager.queryCloudInterface.getFirebaseUser()?.updateEmail(email)
            ?.addOnSuccessListener {
                Log.d(TAG, "User email address updated.")
                listener.onSuccess(null)
            }
            ?.addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    /**
     * Updates the currently authenticated user's password
     */
    fun updateUserPassword(password: String, listener: TaskListener<Unit?>) {
        userManager.queryCloudInterface.getFirebaseUser()?.updatePassword(password)
            ?.addOnSuccessListener {
                Log.d(TAG, "User password update.")
                listener.onSuccess(null)
            }
            ?.addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    fun sendVerificationEmail(listener: TaskListener<Unit?>) {
        userManager.queryCloudInterface.getFirebaseUser()?.let { fbUser ->
            fbUser.sendEmailVerification()
                .addOnSuccessListener {
                    Log.d(TAG, "Email verification sent to ${fbUser.email}")
                    listener.onSuccess(null)
                }
                .addOnFailureListener { e ->
                    listener.onFailure(e)
                }
        }
    }

    fun sendPasswordResetEmail(email: String, listener: TaskListener<Unit?>) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Email password reset sent to ${email}")
                listener.onSuccess(null)
            }
            .addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    /**
     * Deletes the currently authenticated user from auth and FireStore
     */
    fun deleteUser(listener: TaskListener<Unit?>) {
        userManager.queryCloudInterface.getFirebaseUser()?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                    deleteFireStoreUser(listener)
                }
            }
    }

    /**
     * Deletes the currently authenticated user from FireStore
     * Should only be called when also deleting user from authentication
     */
    private fun deleteFireStoreUser(listener: TaskListener<Unit?>) {
        val currUser = userManager.queryCloudInterface.getFirebaseUser()
        if (currUser == null) {
            listener.onFailure(UnauthorizedUserException())
        } else {
            userManager.queryCloudInterface.getUserByUid(currUser.uid, object :
                TaskListener<DocumentSnapshot> {
                override fun onSuccess(value: DocumentSnapshot) {
                    value.reference.delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully deleted.")
                            listener.onSuccess(null)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error deleting FireStoreUser", e)
                            listener.onFailure(e)
                        }
                }

                override fun onFailure(e: Exception?) = listener.onFailure(e)
            })
        }
        // TODO delete sub-collections
    }

    /**
     * Signs the user up, adding them to the authentication server
     * Then a server-side script with create a FireStore user for us to populate later
     */
    fun signUpUser(email: String, password: String, listener: TaskListener<Unit?>) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d(TAG, "Create user with email is successful.")
                listener.onSuccess(null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, e.message ?: "Error signing up a new user")
                listener.onFailure(e)
            }
    }

    companion object {
        private const val TAG = "UserManagementCloudInterface"
    }

}