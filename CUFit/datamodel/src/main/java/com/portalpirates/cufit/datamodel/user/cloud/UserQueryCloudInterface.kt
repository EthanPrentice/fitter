package com.portalpirates.cufit.datamodel.user.cloud

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.CloudInterface
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.exception.FitFirebaseException

internal class UserQueryCloudInterface(manager: Manager) : CloudInterface(manager) {

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

    companion object {
        private const val COLLECTION = "users"
    }

}