package com.portalpirates.cufit.datamodel.user.processing

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.datamodel.adt.DataProcessor
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.exception.UnauthorizedUserException
import com.portalpirates.cufit.datamodel.data.measure.FitMeasure
import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.user.*
import com.portalpirates.cufit.datamodel.user.UserManager
import com.portalpirates.cufit.datamodel.user.cloud.UserQueryCloudInterface
import java.lang.IllegalArgumentException

internal class UserQueryDataProcessor(manager: Manager) : DataProcessor(manager) {

    private val userManager: UserManager
        get() = manager as UserManager

    override val cloudInterface: UserQueryCloudInterface
        get() = userManager.queryCloudInterface


    /**
     * Runs [listener] with the [FitUser] created from the document provided by the [UserCloudInterface]
     * If no User can be created, run the listener with null
     */
    fun getUserByUid(uid: String, listener: TaskListener<FitUser?>) {
        cloudInterface.getUserByUid(uid, object :
            TaskListener<DocumentSnapshot> {
            // If a user is found by the CloudInterface, create a User from the document and call the onSuccessListener
            override fun onSuccess(value: DocumentSnapshot) {
                val user = createUserFromDocument(value)
                listener.onSuccess(user)
            }
            // If a user is not found, or one could not be created from the supplied document, run onSuccess with null
            override fun onFailure(e: Exception?) = listener.onFailure(e)
        })
    }

    fun getAuthenticatedUser(listener: TaskListener<AuthenticatedUser?>) {
        val firebaseUser = cloudInterface.getFirebaseUser()
        if (firebaseUser == null) {
            listener.onFailure(UnauthorizedUserException())
        } else {
            getUserByUid(firebaseUser.uid, object :
                TaskListener<FitUser?> {
                override fun onSuccess(value: FitUser?) {
                    if (value != null) {
                        val authenticatedUser = AuthenticatedUser(firebaseUser, value)
                        listener.onSuccess(authenticatedUser)
                    } else {
                        listener.onFailure(null)
                    }
                }
                override fun onFailure(e: Exception?) = listener.onFailure(e)
            })
        }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return cloudInterface.getFirebaseUser()
    }

    @Throws(IllegalArgumentException::class)
    private fun createUserFromDocument(doc: DocumentSnapshot) : FitUser? {
        return try {
            val currHeight = FitMeasure.getFromDocument(doc,
                UserField.CURRENT_HEIGHT.toString(),
                ::Height
            )
            val currWeight = FitMeasure.getFromDocument(doc,
                UserField.CURRENT_WEIGHT.toString(),
                ::Weight
            )
            val goalWeight = FitMeasure.getFromDocument(doc,
                UserField.WEIGHT_GOAL.toString(),
                ::Weight
            )

            FitUserBuilder()
                .setFirstName(doc.getString(UserField.FIRST_NAME.toString())!!)
                .setLastName(doc.getString(UserField.LAST_NAME.toString())!!)
                .setSex(UserSex.getFromString(doc.getString(UserField.SEX.toString()))!!)
                .setBirthDate(doc.getDate(UserField.BIRTH_DATE.toString())!!)
                .setCurrentHeight(currHeight)
                .setCurrentWeight(currWeight)
                .setWeightGoal(goalWeight)
                .setImageBlob(doc.getBlob(UserField.IMAGE_BMP.toString())?.toBytes())
                .build()

        } catch(e: Exception) {
            Log.e(TAG, "Could not create a user from document")
            null
        }
    }

    companion object {
        private const val TAG = "UserQueryDataProcessor"
    }

}