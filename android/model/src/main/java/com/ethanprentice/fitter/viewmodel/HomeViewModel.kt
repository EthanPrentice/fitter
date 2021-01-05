package com.ethanprentice.fitter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.graph.LineDataConfig
import com.ethanprentice.fitter.datamodel.data.workout.*
import com.ethanprentice.fitter.datamodel.user.UserManager
import com.ethanprentice.fitter.datamodel.workout.WorkoutManager
import com.ethanprentice.fitter.viewmodel.util.ImageSelectorLock
import com.github.mikephil.charting.data.LineDataSet

class HomeViewModel : FitViewModel() {

    /* OTHER */
    val imageSelectorLock = ImageSelectorLock()
    /* END OF OTHER */

    /* GENERAL WORKOUTS */
    fun clearWorkouts() {
        clearRecentWorkouts()
        clearOwnedWorkouts()
    }

    fun createWorkout(builder: WorkoutBuilder, listener: TaskListener<Workout>) {
        WorkoutManager.getInstance().receiver.createWorkout(builder, listener)
    }

    fun updateWorkout(uid: String, fields: HashMap<WorkoutField, Any?>, listener: TaskListener<Unit?>) {
        WorkoutManager.getInstance().receiver.updateWorkout(uid, fields, listener)
    }

    var muscleGroups: List<MuscleGroup> = WorkoutManager.getInstance().provider.getMuscleGroups()
    /* END OF GENERAL WORKOUTS */


    /* RECENT WORKOUTS */
    private val recentWorkoutsList = ArrayList<Workout>()
    private val _recentWorkouts = MutableLiveData<List<Workout>>().apply {
        value = recentWorkoutsList
    }
    val recentWorkouts: LiveData<List<Workout>> = _recentWorkouts

    fun addRecentWorkouts(vararg w: Workout) {
        recentWorkoutsList.addAll(w)
        _recentWorkouts.postValue(_recentWorkouts.value)
    }

    fun removeRecentWorkouts(vararg w: Workout) {
        recentWorkoutsList.removeAll(w)
        _recentWorkouts.postValue(_recentWorkouts.value)
    }

    fun clearRecentWorkouts() {
        recentWorkoutsList.clear()
        _recentWorkouts.postValue(_recentWorkouts.value)
    }
    /* END OF RECENT WORKOUTS */
    


    /* MY WORKOUTS */
    private val ownedWorkoutsList = ArrayList<Workout>()
    private val _ownedWorkouts = MutableLiveData<List<Workout>>().apply {
        value = ownedWorkoutsList
    }
    val ownedWorkouts: LiveData<List<Workout>> = _ownedWorkouts

    fun addOwnedWorkouts(vararg w: Workout) {
        ownedWorkoutsList.addAll(w)
        _ownedWorkouts.postValue(_ownedWorkouts.value)
    }

    fun insertOwnedWorkout(pos: Int, w: Workout) {
        ownedWorkoutsList.add(pos, w)
        _ownedWorkouts.postValue(_ownedWorkouts.value)
    }

    fun removeOwnedWorkouts(vararg w: Workout) {
        ownedWorkoutsList.removeAll(w)
        _ownedWorkouts.postValue(_ownedWorkouts.value)
    }

    fun clearOwnedWorkouts() {
        ownedWorkoutsList.clear()
        _ownedWorkouts.postValue(_ownedWorkouts.value)
    }

    fun getWorkoutsByOwner(ownerUid: String, listener: TaskListener<List<Workout>>) {
        WorkoutManager.getInstance().provider.getWorkoutsByOwner(ownerUid, listener)
    }

    fun getRecentWorkouts(uid: String, listener: TaskListener<List<Workout>>) {
        return WorkoutManager.getInstance().provider.getRecentWorkouts(uid, listener)
    }
    /* END OF MY WORKOUTS */


    /* LOGGING */
    fun createWorkoutLog(builder: WorkoutBuilder, listener: TaskListener<String>) {
        WorkoutManager.getInstance().receiver.createWorkoutLog(builder, listener)
    }

    /* END OF LOGGING */


    /* PROGRESS */

    fun getExerciseDataSet(ownerUid: String, exerciseName: String, config: LineDataConfig?, listener: TaskListener<LineDataSet?>) {
        return WorkoutManager.getInstance().provider.getExerciseDataSet(ownerUid, exerciseName, config, listener)
    }

    /* END OF PROGRESS */


    /* EXERCISES */

    fun getExercises(): List<Exercise> {
        return WorkoutManager.getInstance().provider.getExercises().toMutableList()
    }

    /* END OF EXERCISES */
    
}