package com.portalpirates.cufit.model.providers

import com.portalpirates.cufit.model.processing.UserDataProcessor

class UserProvider : Provider() {

    fun getUser() {
        UserDataProcessor().getUser()
    }

}