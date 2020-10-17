package com.portalpirates.cufit.ui

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.portalpirates.cufit.datamodel.manager.UserManager

class FitApplication : Application() {

    lateinit var userManager: UserManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        userManager = UserManager()

        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        Firebase.firestore.firestoreSettings = settings
    }

    companion object {
        lateinit var instance: FitApplication
            private set
    }

}