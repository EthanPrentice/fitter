package com.portalpirates.cufit.datamodel.workout.provider

import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Provider
import com.portalpirates.cufit.datamodel.adt.TaskListener
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

    fun getExploreWorkouts(): List<Workout> {
        return List(5) {
            // TODO FIX Return actual workouts!
            Workout("Name", "Desc", "Owner UID", null, true, null, ArrayList(), null, null)
        }
    }


    fun getMuscleGroups(): List<MuscleGroup> {
        return listOf("Chest", "Back", "Legs", "Shoulders", "Triceps", "Biceps", "Abs").map { MuscleGroup(it) }
    }

   fun graphPreviousWorkouts(lineDataCfg : LineDataConfig) : LineData {

      // query weights from workout.exercise
       val workoutData = dataProcessor.getPreviousWorkouts()
       val graphData : MutableMap<String, Weight> = mutableMapOf()
       var weightsUsed : List
       workoutData.forEach { w ->
          w.exercise.forEach { e ->
            weightsUsed.add(e.weight)
          }
           graphData.put(w.name, weightsUsed.average())
       }
       // return lineData object
      return LineDataUtil.toLineData(graphData, "Workout Weights")
   }

}