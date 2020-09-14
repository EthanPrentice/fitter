package com.portalpirates.cufit.model.cloud

import com.google.firebase.firestore.DocumentSnapshot

class UserCloudInterface : CloudInterface() {

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


    companion object {
        const val COLLECTION = "user"
    }

}