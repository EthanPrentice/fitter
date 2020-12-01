package com.portalpirates.cufit.datamodel.data.workout

import com.portalpirates.cufit.datamodel.data.measure.Weight

open class Exercise(
        var name: String,
        var description: String? = null,
        var instructions: List<String>? = null,
        var targetMuscleGroups: List<MuscleGroup>? = null,
        var weight: Weight?,
        var sets: Int,
        var reps: Int
) {
    // copy constructor
    constructor(exercise: Exercise) : this(
            exercise.name,
            exercise.description,
            exercise.instructions,
            exercise.targetMuscleGroups,
            exercise.weight,
            exercise.sets,
            exercise.reps
    )
}