package com.ethanprentice.fitter.datamodel.user.cloud

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.CloudInterface
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.user.UserManager

internal class UserAuthCloudInterface(manager: Manager) : CloudInterface(manager) {

    val userManager: UserManager
        get() = manager as UserManager

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
        userManager.queryCloudInterface.getFirebaseUser()?.reauthenticate(credential)
            ?.addOnSuccessListener {
                Log.d(TAG, "User re-authenticated.")
                listener.onSuccess(null)
            }
            ?.addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    companion object {
        private const val TAG = "UserAuthCloudInterface"
    }

}