package com.ethanprentice.fitter.ui.workout

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethanprentice.fitter.datamodel.data.workout.Exercise
import com.ethanprentice.fitter.ui.workout.view.AddExerciseView

class AddExercisesAdapter(val exercises: MutableList<Exercise>) : RecyclerView.Adapter<AddExercisesAdapter.AddExerciseViewHolder>() {

    private val checkedPositions = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddExerciseViewHolder {
        val view = AddExerciseView(parent.context)
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = params

        view.setOnCheckedChangeListener { v, checked ->
            v.exercise?.let {
                val pos = exercises.indexOf(it)
                if (checked) {
                    checkedPositions.add(pos)
                } else {
                    checkedPositions.remove(pos)
                }
            }
            null
        }

        return AddExerciseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: AddExerciseViewHolder, position: Int) {
        holder.exerciseView.exercise = exercises[position]
        holder.exerciseView.isChecked = checkedPositions.contains(position)
    }

    fun getCheckedExercises(): List<Exercise> {
        return checkedPositions.map { exercises[it] }
    }

    fun setCheckedExercise(pos: Int, checked: Boolean) {
        if (checked) {
            checkedPositions.add(pos)
        } else {
            checkedPositions.remove(pos)
        }
    }

    class AddExerciseViewHolder(itemView: AddExerciseView) : RecyclerView.ViewHolder(itemView) {
        val exerciseView: AddExerciseView
            get() = itemView as AddExerciseView
    }

}