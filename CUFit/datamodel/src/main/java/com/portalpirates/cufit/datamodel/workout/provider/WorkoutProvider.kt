package com.portalpirates.cufit.datamodel.workout.provider

import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.adt.Provider
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.FitUser
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

    fun getExploreWorkouts(): List<Workout> {
        return List(5) {
            // TODO FIX Return actual workouts!
            Workout("Name", "Desc", "Owner UID", null, true, null, null, null, null)
        }
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






    // workout dataprocessor , workout cloud interface

}