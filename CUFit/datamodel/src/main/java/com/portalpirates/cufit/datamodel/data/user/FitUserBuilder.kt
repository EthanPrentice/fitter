package com.portalpirates.cufit.datamodel.data.user

import com.portalpirates.cufit.datamodel.data.weight.Weight
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.HashMap

class FitUserBuilder {

    // required
    private var birthDate: Date? = null
    private var firstName: String? = null
    private var lastName: String? = null

    // optional
    private var currentWeight: Weight? = null
    private var weightGoal: Weight? = null

    @Throws(UserBuildException::class)
    fun build() : FitUser {
        if (hasRequiredInputs()) {
            return FitUser(birthDate!!, firstName!!, lastName!!, currentWeight, weightGoal)
        } else {
            throw UserBuildException("All required fields have not been provided for user! Cannot build!")
        }
    }

    fun setCurrentWeight(weight: Weight) : FitUserBuilder {
        currentWeight = weight
        return this
    }

    fun setFirstName(name: String) : FitUserBuilder {
        firstName = name
        return this
    }

    fun setLastName(name: String) : FitUserBuilder {
        lastName = name
        return this
    }

    fun hasRequiredInputs(): Boolean {
        return birthDate != null &&
                firstName != null &&
                lastName != null
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "birth_date" to birthDate,
            "current_weight" to currentWeight,
            "name.first" to firstName,
            "name.last" to lastName,
            "weight_goal" to weightGoal
            // TODO: previous weights
        )
    }

    class UserBuildException(s: String?, cause: Throwable?) : IllegalStateException(s, cause) {
        constructor(s: String?) : this(s, null)
        constructor(cause: Throwable?) : this(null, cause)
        constructor() : this(null, null)
    }

}