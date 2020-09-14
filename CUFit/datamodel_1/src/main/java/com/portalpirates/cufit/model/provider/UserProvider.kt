package com.portalpirates.cufit.model.provider

import com.portalpirates.cufit.model.data.user.User
import com.portalpirates.cufit.model.processing.UserDataProcessor

class UserProvider : Provider() {

    private val userDataProcessor: UserDataProcessor
        get() = dataProcessor as UserDataProcessor


    /**
     * Runs [callback] with the [User]? provided by [UserDataProcessor]
     */
    fun getUserByUid(uid: String, callback: (user: User?) -> Unit) {
        userDataProcessor.getUserByUid(uid, callback)
    }

    companion object {
        private var actualInstance: UserProvider? = null
        val instance: UserProvider
            get() {
            if (actualInstance == null) {
                actualInstance = UserProvider()
            }
            return actualInstance!!
        }
    }
}