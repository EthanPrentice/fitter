package com.ethanprentice.fitter.ui

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class FitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

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