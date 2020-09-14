package com.portalpirates.cufit.model.processing

import com.google.firebase.firestore.DocumentSnapshot
import com.portalpirates.cufit.model.cloud.UserCloudInterface
import com.portalpirates.cufit.model.data.User
import com.portalpirates.cufit.model.data.Weight
import java.util.*

class UserDataProcessor : DataProcessor() {

    private var currentUser : User = getUser()

    fun getUser() : User {
        val user = UserCloudInterface().getUser()
        if (user != null) {
            currentUser = User(user.uid)
            val fireStore = UserCloudInterface().getFireStoreUser() as DocumentSnapshot
            currentUser.birthDate = fireStore["birth_date"] as Date
            currentUser.age = fireStore["age"] as Int
            currentUser.firstName = fireStore["name.first"] as String
            currentUser.lastName = fireStore["name.last"] as String
            currentUser.currentWeight = fireStore["current_weight"] as Weight
            currentUser.weightGoal = fireStore["weight_goal"] as Weight
            // TODO: previous weights
            return currentUser
        }
        return User()
    }

    fun createUser(birthDate: Date, age: Int, firstName: String, lastName: String, currentWeight: Weight, weightGoal: Weight) {
        currentUser.birthDate = birthDate
        currentUser.age = age
        currentUser.firstName = firstName
        currentUser.lastName = lastName
        currentUser.currentWeight = currentWeight
        currentUser.weightGoal = weightGoal
        currentUser.addPreviousWeight(currentWeight)

        val user = hashMapOf(
            "age" to currentUser.age,
            "birth_date" to currentUser.birthDate,
            "current_weight" to currentUser.currentWeight,
            "name.first" to currentUser.firstName,
            "name.last" to currentUser.lastName,
            "weight_goal" to currentUser.weightGoal
            // TODO: previous weights
        )

        UserCloudInterface().createUser(user)
    }

    fun updateUser(birthDate: Date, age: Int, firstName: String, lastName: String, currentWeight: Weight, weightGoal: Weight) {
        currentUser.addPreviousWeight(currentUser.currentWeight)
        currentUser.birthDate = birthDate
        currentUser.age = age
        currentUser.firstName = firstName
        currentUser.lastName = lastName
        currentUser.currentWeight = currentWeight
        currentUser.weightGoal = weightGoal

        val user = hashMapOf(
            "age" to currentUser.age,
            "birth_date" to currentUser.birthDate,
            "current_weight" to currentUser.currentWeight,
            "name.first" to currentUser.firstName,
            "name.last" to currentUser.lastName,
            "weight_goal" to currentUser.weightGoal
            // TODO: previous weights
        )

        UserCloudInterface().updateUser(user)
    }

    fun updateUserEmail(email: String) {
        UserCloudInterface().updateUserEmail(email)
    }

    fun updateUserPassword(password: String) {
        UserCloudInterface().updateUserPassword(password)
    }

    fun sendVerificationEmail() {
        UserCloudInterface().sendVerificationEmail()
    }

    fun sendPasswordResetEmail(email: String) {
        UserCloudInterface().sendPasswordResetEmail(email)
    }

    fun deleteUser() {
        UserCloudInterface().deleteUser()
        UserCloudInterface().deleteFireStoreUser()
    }

    fun authenticateUser(email: String, password: String) {
        UserCloudInterface().authenticateUser(email, password)
    }

}