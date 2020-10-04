package com.portalpirates.cufit.datamodel.data.user

import android.graphics.Bitmap
import com.portalpirates.cufit.datamodel.FitException
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface
import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.measure.Weight
import java.util.*
import kotlin.collections.HashMap

class FitUserBuilder {

    // required
    var birthDate: Date? = null
    var firstName: String? = null
    var lastName: String? = null
    var sex: UserSex? = null

    // optional
    var imageBmp: Bitmap? = null
    var currentWeight: Weight? = null
    var currentHeight: Height? = null
    var weightGoal: Weight? = null

    @Throws(UserBuildException::class)
    fun build(): FitUser {
        if (hasRequiredInputs()) {
            return FitUser(birthDate!!, firstName!!, lastName!!, currentWeight, currentHeight, weightGoal)
        } else {
            throw UserBuildException("All required fields have not been provided for user! Cannot build!")
        }
    }

    fun setCurrentWeight(weight: Weight?) : FitUserBuilder {
        currentWeight = weight
        return this
    }

    fun setCurrentHeight(height: Height?) : FitUserBuilder {
        currentHeight = height
        return this
    }

    fun setWeightGoal(weight: Weight?) : FitUserBuilder {
        weightGoal = weight
        return this
    }

    fun setBirthDate(date: Date): FitUserBuilder {
        birthDate = date
        return this
    }

    fun setFirstName(name: String): FitUserBuilder {
        firstName = name
        return this
    }

    fun setLastName(name: String): FitUserBuilder {
        lastName = name
        return this
    }

    fun setSex(sex: UserSex): FitUserBuilder {
        this.sex = sex
        return this
    }

    fun setImage(bmp: Bitmap?): FitUserBuilder {
        imageBmp = bmp
        return this
    }

    fun hasRequiredInputs(): Boolean {
        return birthDate != null &&
                firstName != null &&
                lastName != null &&
                sex != null
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        val hashMap = hashMapOf<String, Any?>(
            UserCloudInterface.BIRTH_DATE to birthDate,
            UserCloudInterface.FIRST_NAME to firstName,
            UserCloudInterface.LAST_NAME to lastName,
            UserCloudInterface.SEX to sex!!.char.toString()
            // TODO: previous weights
            // TODO: previous heights
        )

        currentWeight?.addFieldsToHashMap(hashMap, UserCloudInterface.CURRENT_WEIGHT)
        currentHeight?.addFieldsToHashMap(hashMap, UserCloudInterface.CURRENT_HEIGHT)
        weightGoal?.addFieldsToHashMap(hashMap, UserCloudInterface.WEIGHT_GOAL)

        return hashMap
    }

    class UserBuildException(s: String, cause: Throwable?) : FitException(s, cause) {
        constructor(s: String) : this(s, null)
    }

}