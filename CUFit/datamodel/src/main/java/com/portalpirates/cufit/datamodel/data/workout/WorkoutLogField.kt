package com.portalpirates.cufit.datamodel.data.workout

enum class WorkoutLogField(val fieldName: String) {
    UID("uid"),
    WORKOUT_UID("workout_uid"),
    DATE("date"),
    OWNER_UID("owner_uid"),
    EXERCISES("exercises");

    override fun toString(): String {
        return fieldName
    }
}