package com.portalpirates.cufit.datamodel.data.workout

import com.portalpirates.cufit.datamodel.data.measure.FitMeasure
import com.portalpirates.cufit.datamodel.data.measure.Weight

open class Exercise(
        var name: String,
        var weight: Weight?,
        var sets: Int,
        var reps: Int
) {
    // copy constructor
    constructor(exercise: Exercise) : this(
            exercise.name,
            exercise.weight,
            exercise.sets,
            exercise.reps
    )

    constructor(hashMap: HashMap<String, Any?>) : this (
        hashMap[NAME] as String,
        FitMeasure.getFromHashMap(hashMap[WEIGHT] as HashMap<String, Any?>, ::Weight),
        (hashMap[SETS] as Long).toInt(),
        (hashMap[REPS] as Long).toInt()
    )

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            NAME to name,
            WEIGHT to weight?.convertFieldsToHashMap(),
            SETS to sets,
            REPS to reps
        )
    }

    companion object {
        const val NAME = "name"
        const val WEIGHT = "weight"
        const val SETS = "sets"
        const val REPS = "reps"
    }
}