package com.portalpirates.cufit.model.cloud

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

abstract class CloudInterface {

    val db = Firebase.firestore
    val auth = Firebase.auth


    companion object {
        // Store all common field names as constants here
        // Any unique field names, store in their corresponding CloudInterface subclass
        const val UID = "uid"
    }

}