package com.portalpirates.cufit.datamodel.workout.cloud

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.portalpirates.cufit.datamodel.adt.CloudInterface
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.datamodel.exception.FitFirebaseException

internal class WorkoutQueryCloudInterface(manager: Manager) : CloudInterface(manager) {

    fun getWorkoutByUid(uid: String, listener: TaskListener<DocumentSnapshot>) {
        db.collection(COLLECTION).document(uid).get()
            .addOnSuccessListener { result ->
                if (result == null) {
                    listener.onFailure(
                        FitFirebaseException(
                            "Could not find a Workout with UID=$uid"
                        )
                    )
                }
                listener.onSuccess(result)
            }
            .addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }


    fun getWorkoutsByOwner(ownerUid: String, listener: TaskListener<QuerySnapshot>) {
        db.collection(COLLECTION).whereEqualTo(WorkoutField.OWNER.toString(), ownerUid).get()
            .addOnSuccessListener { result ->
                listener.onSuccess(result)
            }
            .addOnFailureListener { e ->
                listener.onFailure(e)
            }
    }

    companion object {
        private const val COLLECTION = "workouts"
    }
}