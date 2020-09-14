package com.portalpirates.cufit.model.receivers

import com.portalpirates.cufit.model.data.Weight
import com.portalpirates.cufit.model.processing.UserDataProcessor
import java.util.*

class UserReceiver : Receiver() {

    fun createUser(birthDate: Date, age: Int, firstName: String, lastName: String, currentWeight: Weight, weightGoal: Weight) {
        UserDataProcessor().createUser(birthDate, age, firstName, lastName, currentWeight, weightGoal)
    }

    fun updateUser(birthDate: Date, age: Int, firstName: String, lastName: String, currentWeight: Weight, weightGoal: Weight) {
        UserDataProcessor().updateUser(birthDate, age, firstName, lastName, currentWeight, weightGoal)
    }

    fun updateUserEmail(email: String) {
        UserDataProcessor().updateUserEmail(email)
    }

    fun updateUserPassword(password: String) {
        UserDataProcessor().updateUserPassword(password)
    }

    fun sendVerificationEmail() {
        UserDataProcessor().sendVerificationEmail()
    }

    fun sendPasswordResetEmail(email: String) {
        UserDataProcessor().sendPasswordResetEmail(email)
    }

    fun deleteUser() {
        UserDataProcessor().deleteUser()
    }

    fun authenticateUser(email: String, password: String) {
        UserDataProcessor().authenticateUser(email, password)
    }


}