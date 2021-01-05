package com.ethanprentice.fitter.viewmodel

import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.user.UserManager

class AuthViewModel : FitViewModel() {

    fun authenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        UserManager.getInstance().authReceiver.authenticateUser(email, password, listener)
    }

    fun reAuthenticateUser(email: String, password: String, listener: TaskListener<Unit?>) {
        UserManager.getInstance().authReceiver.reAuthenticateUser(email, password, listener)
    }

    fun signUpUser(email: String, password: String, listener: TaskListener<Unit?>) {
        UserManager.getInstance().managementReceiver.signUpUser(email, password, listener)
    }

}