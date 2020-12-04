package com.portalpirates.cufit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portalpirates.cufit.datamodel.data.workout.MuscleGroup
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.ui.util.ImageSelectorLock

class HomeViewModel : ViewModel() {

    /* OTHER */
    val imageSelectorLock = ImageSelectorLock()
    /* END OF OTHER */

    /* GENERAL WORKOUTS */
    fun clearWorkouts() {
        clearRecentWorkouts()
        clearOwnedWorkouts()
    }

    var muscleGroups: List<MuscleGroup> = ArrayList()
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
    /* END OF MY WORKOUTS */
    
    
}