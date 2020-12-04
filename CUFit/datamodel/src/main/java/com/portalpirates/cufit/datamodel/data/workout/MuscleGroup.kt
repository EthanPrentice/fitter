package com.portalpirates.cufit.datamodel.data.workout

class MuscleGroup(
        var name: String
) {
    // copy constructor
    constructor(muscleGroup: MuscleGroup) : this(
            muscleGroup.name
    )

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is MuscleGroup -> name == other.name
            else -> false
        }
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        return hashMapOf(NAME to name)
    }

    companion object {
        const val NAME = "name"
    }
}