package com.portalpirates.cufit.datamodel.provider

import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseUser
import com.portalpirates.cufit.datamodel.cloud.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor
import com.portalpirates.cufit.datamodel.util.chart.LineDataUtil
import java.util.*

class UserProvider(manager: Manager) : Provider(manager) {

    private val userDataProcessor: UserDataProcessor
        get() = manager.dataProcessor as UserDataProcessor

    private var cachedAuthenticatedUser: AuthenticatedUser? = null

    /**
     * Runs [callback] with the [FitUser]? provided by [UserDataProcessor]
     */
    fun getUserByUid(uid: String, listener: TaskListener<FitUser?>) {
        userDataProcessor.getUserByUid(uid, listener)
    }

    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>, allowCached: Boolean = true) {
        if (allowCached && cachedAuthenticatedUser != null) {
            listener.onSuccess(cachedAuthenticatedUser)
            return
        }

        userDataProcessor.getAuthenticatedUser(object : TaskListener<AuthenticatedUser?> {
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
        return userDataProcessor.getFirebaseUser()
    }

    fun getRecentWorkouts(): List<Workout> {
        // TODO: un-hardcode this after the demo
        return List(5) {
            Workout()
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