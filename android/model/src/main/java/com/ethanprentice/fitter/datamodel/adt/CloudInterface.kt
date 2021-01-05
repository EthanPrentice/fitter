package com.ethanprentice.fitter.datamodel.adt

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

internal abstract class CloudInterface(protected val manager: Manager) {

    val db = Firebase.firestore
    val auth = Firebase.auth
    
    companion object {
        // Store all common field names as constants here
        // Any unique field names, store in their corresponding CloudInterface subclass
        const val UID = "uid"
    }

}