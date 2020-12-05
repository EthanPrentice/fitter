package com.portalpirates.cufit.ui.workout

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.ui.workout.view.WorkoutCardView

class WorkoutCardAdapter(private val workouts: List<Workout>) : RecyclerView.Adapter<WorkoutCardAdapter.WorkoutCardViewHolder>() {

    private var onWorkoutLoggedListener: ((Workout) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutCardViewHolder {
        val view = WorkoutCardView(parent.context).apply {
            setOnWorkoutLoggedListener(onWorkoutLoggedListener)
        }
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = params
        return WorkoutCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    override fun onBindViewHolder(holder: WorkoutCardViewHolder, position: Int) {
        holder.workoutCardView.setWorkout(workouts[position])
    }

    fun setOnWorkoutLoggedListener(listener: ((Workout) -> Unit?)?) {
        onWorkoutLoggedListener = listener
    }

    class WorkoutCardViewHolder(itemView: WorkoutCardView) : RecyclerView.ViewHolder(itemView) {
        val workoutCardView: WorkoutCardView
            get() = itemView as WorkoutCardView
    }
}