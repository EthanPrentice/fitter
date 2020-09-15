package com.portalpirates.cufit.datamodel.cloud

import android.util.Log
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

    fun authenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
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
    }

}