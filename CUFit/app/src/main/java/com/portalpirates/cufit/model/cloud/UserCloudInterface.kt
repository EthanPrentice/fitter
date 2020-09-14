package com.portalpirates.cufit.model.cloud

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserCloudInterface : CloudInterface() {

    private val auth : FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val collection = db.collection("users")

    fun getUser() : FirebaseUser? {
        return auth.currentUser
    }

    fun getFireStoreUser() {
        collection.document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    return@addOnSuccessListener document as Unit
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { e -> Log.d(TAG, "get failed with ", e) }
    }

    fun createUser(user: HashMap<String, Any>) {
        collection.document(auth.currentUser!!.uid)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun updateUser(user: HashMap<String, Any>) {
        collection.document(auth.currentUser!!.uid)
            .update(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun updateUserEmail(email: String) {
        getUser()!!.updateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
    }

    fun updateUserPassword(password: String) {
        getUser()!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    fun sendVerificationEmail() {
        getUser()!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    fun deleteUser() {
        getUser()!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }

    fun deleteFireStoreUser() {
        collection.document(auth.currentUser!!.uid)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        // TODO delete sub-collections
    }

    fun authenticateUser(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getUser()!!.reauthenticate(credential)
            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
    }

}