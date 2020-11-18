package com.portalpirates.cufit.datamodel.cloud

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.cloud.exception.FitFirebaseException
import com.portalpirates.cufit.datamodel.cloud.exception.UnauthorizedUserException
import com.portalpirates.cufit.datamodel.manager.Manager

internal class UserCloudInterface(manager: Manager) : CloudInterface(manager) {

    fun getFirebaseUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUserByUid(uid: String, listener: TaskListener<DocumentSnapshot>) {
        db.collection(COLLECTION).document(uid).get()
            .addOnSuccessListener { result ->
                if (result == null) {
                    listener.onFailure(
                        FitFirebaseException(
                            "Could not find a FitUser with UID=$uid"
                        )
                    )
                }
                listener.onSuccess(result)
            }
            .addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    /**
     * Creates a user in FireStore.  This user has already been created from a server-side script after an auth user is created
     * so we just populate it here
     */
    fun createFireStoreUser(fields: HashMap<String, Any?>, listener: TaskListener<Unit?>) {
        val currUser = getFirebaseUser()
        if (currUser == null) {
            listener.onFailure(UnauthorizedUserException())
        } else {
            getUserByUid(currUser.uid, object : TaskListener<DocumentSnapshot> {
                override fun onSuccess(value: DocumentSnapshot) {
                    value.reference.update(fields)
                        .addOnSuccessListener {
                            Log.d(TAG, "FireStoreUser successfully created.")
                            listener.onSuccess(null)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error creating FireStoreUser", e)
                            listener.onSuccess(null)
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
        val currUser = getFirebaseUser()
        if (currUser == null) {
            listener.onFailure(null)
        } else {
            getUserByUid(currUser.uid, object : TaskListener<DocumentSnapshot> {
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
        getFirebaseUser()?.updateEmail(email)
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
        getFirebaseUser()?.updatePassword(password)
            ?.addOnSuccessListener {
                Log.d(TAG, "User password update.")
                listener.onSuccess(null)
            }
            ?.addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    fun sendVerificationEmail(listener: TaskListener<Unit?>) {
        getFirebaseUser()?.let { fbUser ->
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
        getFirebaseUser()?.delete()
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
        val currUser = getFirebaseUser()
        if (currUser == null) {
            listener.onFailure(UnauthorizedUserException())
        } else {
            getUserByUid(currUser.uid, object : TaskListener<DocumentSnapshot> {
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

    fun authenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d(TAG, "Sign in with email is successful.")
                listener.onSuccess(null)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Sign in with email failed.", e)
                listener.onFailure(e)
            }
    }

    fun reAuthenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getFirebaseUser()?.reauthenticate(credential)
            ?.addOnSuccessListener {
                Log.d(TAG, "User re-authenticated.")
                listener.onSuccess(null)
            }
            ?.addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    companion object {
        const val TAG = "UserCloudInterface"

        const val COLLECTION = "users"

        // Fields
        const val IMAGE_BMP = "avatar_blob"
        const val BIRTH_DATE = "birth_date"
        const val FIRST_NAME = "name.first"
        const val LAST_NAME = "name.last"
        const val CURRENT_WEIGHT = "current_weight"
        const val CURRENT_HEIGHT = "current_height"
        const val WEIGHT_GOAL = "weight_goal"
        const val SEX = "gender"
    }

}