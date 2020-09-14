package com.portalpirates.cufit.model.processing

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.model.cloud.UserCloudInterface
import com.portalpirates.cufit.model.data.user.User
import com.portalpirates.cufit.model.provider.UserProvider
import java.lang.Exception
import java.util.*

class UserDataProcessor : DataProcessor(UserCloudInterface(), UserProvider()) {

    private val userCloudInterface: UserCloudInterface
        get() = cloudInterface as UserCloudInterface


    /**
     * Runs [callback] with the [User] created from the document provided by the [UserCloudInterface]
     * If no User can be created, run the callback with null
     */
    fun getUserByUid(uid: String, callback: (user: User?) -> Unit) {
        userCloudInterface.getUserByUid(uid, { doc ->
            // If a user is found by the CloudInterface, create a User from the document and call the onSuccessListener
            if (doc != null) {
                val user = createUserFromDocument(doc)
                if (user != null) {
                    callback(user)
                }
            }
            // If a user is not found, or one could not be created from the supplied document, run onSuccess with null
            callback(null)
        })
    }

    private fun createUserFromDocument(doc: DocumentSnapshot): User? {
        return try {
            User(
                doc.data?.get("birth_date") as Date,
                doc.data?.get("name/first") as String,
                doc.data?.get("name/last") as String
            )
        } catch(e: Exception) {
            Log.e(TAG, "Could not create a user from document")
            null
        }
    }


    companion object {
        const val TAG = "UserDataProcessor"
    }

}