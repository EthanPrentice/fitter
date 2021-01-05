package com.ethanprentice.fitter.viewmodel

import androidx.lifecycle.ViewModel
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.user.AuthenticatedUser
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.google.firebase.auth.FirebaseUser

abstract class FitViewModel : ViewModel()  {

    val firebaseUser: FirebaseUser?
        get() = UserManager.getInstance().provider.getFirebaseUser()

    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>, allowCached: Boolean = true) {
        UserManager.getInstance().provider.getAuthenticatedUser(listener, allowCached)
    }

}