package com.portalpirates.cufit.model.cloud

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserCloudInterface : CloudInterface() {

    fun getUser() : FirebaseUser? {
        return Firebase.auth.currentUser
    }

    fun createUser() {

    }

    fun updateUser() {

    }

    fun updateUserEmail() {

    }

    fun updateUserPassword() {

    }

    fun deleteUser() {

    }

    fun authenticateUser() {

    }

}