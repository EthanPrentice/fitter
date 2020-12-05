package com.portalpirates.cufit.datamodel.workout.provider

import android.icu.util.MeasureUnit
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Provider
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.datamodel.data.workout.MuscleGroup
import com.portalpirates.cufit.datamodel.workout.WorkoutManager
//import com.github.mikephil.charting.data.LineData
import com.portalpirates.cufit.datamodel.util.chart.LineDataUtil
import com.portalpirates.cufit.datamodel.data.graph.LineDataConfig
import com.portalpirates.cufit.datamodel.workout.processing.WorkoutQueryDataProcessor
import java.util.*

class WorkoutProvider(manager: Manager) : Provider(manager) {

    private val workoutManager: WorkoutManager
        get() = manager as WorkoutManager

    override val dataProcessor: WorkoutQueryDataProcessor
        get() = workoutManager.queryDataProcessor

    fun getWorkoutByUid(uid: String, listener: TaskListener<Workout>) {
        dataProcessor.getWorkoutByUid(uid, listener)
    }

    fun getWorkoutsByOwner(ownerUid: String, listener: TaskListener<List<Workout>>) {
        dataProcessor.getWorkoutsByOwner(ownerUid, listener)
    }

    fun getWorkoutLogByUid( ownerUid: String, workoutLogUid: String, listener: TaskListener<Workout>) {
        dataProcessor.getWorkoutLogByUid(ownerUid, workoutLogUid, listener)
    }

    fun getWorkoutLogsByOwnerId( ownerUid: String, listener: TaskListener<List<Workout>>) {
        dataProcessor.getWorkoutLogsByOwnerId(ownerUid, listener)
    }

    fun getExploreWorkouts(): List<Workout> {
        return List(5) {
            // TODO FIX Return actual workouts!
            Workout("Name", "Desc", "Owner UID", null, true, null, ArrayList(), null, null)
        }
    }


    fun getMuscleGroups(): List<MuscleGroup> {
        return listOf("Chest", "Back", "Legs", "Shoulders", "Triceps", "Biceps", "Forearms", "Abs").map { MuscleGroup(it) }
    }

    fun getExercises(): List<Exercise> {

        fun toMGs(vararg list: String) = list.map { MuscleGroup(it) }

        return listOf(
            // CHEST
            Exercise("Bench Press", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Chest", "Triceps")),
            Exercise("Pec Flys", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Chest")),
            Exercise("Squeeze Press", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Chest", "Triceps")),

            // TRICEPS
            Exercise("Tricep Extensions", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Triceps")),

            // BACK
            Exercise("Lat Pulldown", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Back", "Biceps", "Shoulders")),
            Exercise("Seated Row",   Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Back", "Biceps")),
            Exercise("Straight-arm Pulldown", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Back")),

            // BICEPS
            Exercise("Incline DB Curls", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Biceps")),
            Exercise("Hammer Curls", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Biceps")),
            Exercise("Reverse BB Curls", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Forearms")),

            // LEGS
            Exercise("Squats",       Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Legs", "Abs")),
            Exercise("Calf Raises",  Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Legs")),
            Exercise("Hamstring Curls", Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Legs")),

            // SHOULDERS
            Exercise("OHP",          Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Shoulders")),
            Exercise("Delt Flys",   Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Shoulders")),
            Exercise("Side Raises",  Weight(0, MeasureUnit.POUND, Date()), 0, 0, toMGs("Shoulders", "Forearms"))
        )
    }

   fun getExerciseDataSet(ownerUid: String, exerciseName: String, config: LineDataConfig, listener: TaskListener<List<Workout>>): LineDataSet {
       val exerciseWeights = dataProcessor.getLoggedExerciseWeights(exerciseName, ownerUid, listener)
       val graphData: Map<Int, Float> = exerciseWeights.mapIndexed { i, weight ->
           i to (weight.number as Float)
       }.toMap()

      return LineDataUtil.toLineDataSet(graphData, exerciseName)
   }

}