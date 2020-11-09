package com.portalpirates.cufit.datamodel.cloud

import com.portalpirates.cufit.datamodel.manager.Manager

internal class WorkoutCloudInterface(manager: Manager) : CloudInterface(manager) {

    companion object {

        // Fields
        const val NAME = "title"
        const val OWNER = "owner"
        const val PUBLIC = "public"
        const val IMAGE_BMP = "workout_blob"
        const val DESCRIPTION = "description"
        const val SUBSCRIBERS = "subscribers"
        const val EXERCISES = "exercises"
        const val TARGET_MUSCLE_GROUPS = "targeted_muscle_groups"
    }
}