package com.portalpirates.cufit.datamodel.data.exercise

import com.portalpirates.cufit.datamodel.data.musclegroup.MuscleGroup

open class Exercise(
        var name: String,
        var description: String? = null,
        var instructions: List<String>? = null,
        var targetMuscleGroups: List<MuscleGroup>? = null
) {
    // copy constructor
    constructor(exercise: Exercise) : this(
            exercise.name,
            exercise.description,
            exercise.instructions,
            exercise.targetMuscleGroups
    )
}