package com.ethanprentice.fitter.ui

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.ethanprentice.fitter.datamodel.workout.WorkoutManager

class FitApplication : Application() {

    lateinit var userManager: UserManager
    lateinit var workoutManager: WorkoutManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        userManager = UserManager()
        workoutManager =
            WorkoutManager()

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