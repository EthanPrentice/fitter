package com.ethanprentice.fitter.datamodel.user.provider

import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseUser
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.user.AuthenticatedUser
import com.ethanprentice.fitter.datamodel.data.user.FitUser
import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.adt.Provider
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.ethanprentice.fitter.datamodel.user.processing.UserQueryDataProcessor
import com.ethanprentice.fitter.datamodel.util.chart.LineDataUtil
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