package com.portalpirates.cufit.datamodel.data.workout

enum class WorkoutField(val fieldName: String) {
    UID("uid"),
    NAME("title"),
    OWNER("owner"),
    PUBLIC("public"),
    IMAGE_BMP("workout_blob"),
    DESCRIPTION("description"),
    SUBSCRIBERS("subscribers"),
    EXERCISES("exercises"),
    TARGET_MUSCLE_GROUPS("targeted_muscle_groups"),
    DATE_LOGGED("date_logged");

    override fun toString(): String {
        return fieldName
    }
}