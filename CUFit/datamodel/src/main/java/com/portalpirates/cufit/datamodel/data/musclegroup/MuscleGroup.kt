package com.portalpirates.cufit.datamodel.data.musclegroup

open class MuscleGroup(
        var name: String,
        var description: String? = null
) {
    // copy constructor
    constructor(muscleGroup: MuscleGroup) : this(
            muscleGroup.name,
            muscleGroup.description
    )
}