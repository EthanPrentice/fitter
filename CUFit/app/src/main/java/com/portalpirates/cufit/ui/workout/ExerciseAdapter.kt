package com.portalpirates.cufit.ui.workout

import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.ui.util.DragEventListener
import com.portalpirates.cufit.ui.util.item_touch.ItemTouchHelperAdapter
import com.portalpirates.cufit.ui.workout.view.ExerciseView

class ExerciseAdapter(private var exercises: MutableList<Exercise>, private val dragEventListener: DragEventListener) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = ExerciseView(parent.context).apply {

        }
        val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = params
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.exerciseView.setOnFieldChangeListener(object : ExerciseView.OnFieldChangedListener {
            override fun onRepsChanged(reps: Int) {
                exercises[position].reps = reps
            }
            override fun onSetsChanged(sets: Int) {
                exercises[position].sets = sets
            }
            override fun onWeightChanged(weight: Weight) {
                exercises[position].weight = weight
            }
        })
        holder.exerciseView.exercise = exercises[position]

        holder.handleView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                dragEventListener.onStartDrag(holder)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                dragEventListener.onEndDrag(holder)
            }
            false
        }
    }

    override fun onItemDismiss(position: Int) {
        exercises.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev: Exercise = exercises.removeAt(fromPosition)
        exercises.add(toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun setExercises(exercises: MutableList<Exercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    class ExerciseViewHolder(itemView: ExerciseView) : RecyclerView.ViewHolder(itemView) {
        val exerciseView: ExerciseView
            get() = itemView as ExerciseView

        val handleView = itemView.findViewById<ImageView>(R.id.handle)
    }
}