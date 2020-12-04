package com.portalpirates.cufit.datamodel.data.workout

import com.portalpirates.cufit.datamodel.data.measure.FitMeasure
import com.portalpirates.cufit.datamodel.data.measure.Weight

open class Exercise(
        var name: String,
        var weight: Weight?,
        var sets: Int,
        var reps: Int,
        var muscleGroups: List<MuscleGroup>? = null
) {


    // copy constructor
    constructor(exercise: Exercise) : this(
            exercise.name,
            exercise.weight,
            exercise.sets,
            exercise.reps,
            exercise.muscleGroups
    )

    constructor(hashMap: HashMap<String, Any?>) : this (
        hashMap[NAME] as String,
        FitMeasure.getFromHashMap(hashMap[WEIGHT] as HashMap<String, Any?>, ::Weight),
        (hashMap[SETS] as Long).toInt(),
        (hashMap[REPS] as Long).toInt(),
        (hashMap[MUSCLE_GROUPS] as? List<String>)?.map { MuscleGroup(it) }
    )

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            NAME to name,
            WEIGHT to weight?.convertFieldsToHashMap(),
            SETS to sets,
            REPS to reps,
            MUSCLE_GROUPS to muscleGroups?.map { it.name }
        )
    }

    companion object {
        const val NAME = "name"
        const val WEIGHT = "weight"
        const val SETS = "sets"
        const val REPS = "reps"
        const val MUSCLE_GROUPS = "muscle_groups"
    }
}