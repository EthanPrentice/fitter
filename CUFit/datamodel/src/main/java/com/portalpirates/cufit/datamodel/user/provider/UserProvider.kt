package com.portalpirates.cufit.datamodel.user.provider

import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseUser
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Provider
import com.portalpirates.cufit.datamodel.user.UserManager
import com.portalpirates.cufit.datamodel.user.processing.UserQueryDataProcessor
import com.portalpirates.cufit.datamodel.util.chart.LineDataUtil
import java.util.*

class UserProvider(manager: Manager) : Provider(manager) {

    private val userManager: UserManager
        get() = manager as UserManager

    override val dataProcessor: UserQueryDataProcessor
        get() = userManager.queryDataProcessor

    private var cachedAuthenticatedUser: AuthenticatedUser? = null


    /**
     * Runs [callback] with the [FitUser]? provided by [UserDataProcessor]
     */
    fun getUserByUid(uid: String, listener: TaskListener<FitUser?>) {
        dataProcessor.getUserByUid(uid, listener)
    }

    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>, allowCached: Boolean = true) {
        if (allowCached && cachedAuthenticatedUser != null) {
            listener.onSuccess(cachedAuthenticatedUser)
            return
        }

        dataProcessor.getAuthenticatedUser(object :
            TaskListener<AuthenticatedUser?> {
            override fun onSuccess(value: AuthenticatedUser?) {
                cachedAuthenticatedUser = value
                listener.onSuccess(value)
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })
    }

    fun getFirebaseUser(): FirebaseUser? {
        return dataProcessor.getFirebaseUser()
    }

    fun getRecentWorkouts(): List<Workout> {
        // TODO: un-hardcode this after the demo
        return List(5) {
            // TODO FIX Return actual workouts!
            Workout("Test", "Test", FitUser(
                    Date(), "Test", "Test", null, null, null, null
            ), true, null, null, null, null)
        }
    }

    fun getBodyWeightLineDataSet(startDate: Date? = null, endDate: Date? = null) : LineDataSet {
        // TODO: after demo, un-hardcode this
        val data = mapOf(
            1 to 164,
            2 to 165,
            3 to 163,
            4 to 161,
            5 to 162,
            6 to 164,
            7 to 164,
            8 to 163,
            9 to 163,
            10 to 161,
            11 to 160
        )
        return LineDataUtil.toLineDataSet(data, "Body weight")
    }

}