package com.portalpirates.cufit.ui.workout.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.data.workout.WorkoutField
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.util.DragEventListener
import com.portalpirates.cufit.ui.util.item_touch.SimpleItemTouchHelperCallback
import com.portalpirates.cufit.ui.view.FitButton
import com.portalpirates.cufit.ui.view.FitCardView
import com.portalpirates.cufit.ui.workout.AddExerciseDialogFragment
import com.portalpirates.cufit.ui.workout.ExerciseAdapter
import kotlinx.android.synthetic.main.button_layout.view.*


class WorkoutCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle), DragEventListener {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var workout: Workout? = null
        private set

    private var expanded = false

    private var workoutTitleView: TextView
    private var workoutImageView: ImageView
    private var chevron: ImageView


    // Collapsed only
    private var workoutDescriptionView: TextView
    private var chipGroup: ChipGroup

    // Expanded only
    private var exercisesView: RecyclerView
    private var logWorkoutBtn: FitButton
    private var addExerciseBtn: FitButton

    // Adapter stuff
    private var exerciseAdapter: ExerciseAdapter? = null
        set(value) {
            field = value
            exercisesView.adapter = value
            val callback = SimpleItemTouchHelperCallback(value!!)
            touchHelper = ItemTouchHelper(callback).apply {
                attachToRecyclerView(exercisesView)
            }
        }
    private var touchHelper: ItemTouchHelper? = null


    init {
        val inflater = LayoutInflater.from(context)
        val content = inflater.inflate(R.layout.workout_card_layout, rootView, false)

        workoutTitleView = content.findViewById(R.id.workout_title)
        workoutDescriptionView = content.findViewById(R.id.workout_desc)
        chevron = content.findViewById(R.id.workout_chevron)
        chevron.setOnClickListener {
            expand()
        }

        workoutImageView = content.findViewById(R.id.workout_img)
        chipGroup = content.findViewById(R.id.chip_group)

        exercisesView = content.findViewById<RecyclerView>(R.id.exercise_list).apply {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically() = false
                override fun isAutoMeasureEnabled() = true
            }
            setHasFixedSize(false)
            isNestedScrollingEnabled = false

            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }

        logWorkoutBtn = content.findViewById(R.id.log_workout_btn)
        addExerciseBtn = content.findViewById(R.id.add_exercise_btn)
        // override text style
        logWorkoutBtn.btn_text_view.setTextAppearance(R.style.body_text)
        addExerciseBtn.btn_text_view.setTextAppearance(R.style.body_text)
        // add listener
        addExerciseBtn.setOnClickListener {
            showAddExercisesDialog()
        }

        setContentView(content)
    }

    fun collapse() {
        expanded = false

        chevron.apply {
            setImageResource(R.drawable.ic_chevron_down)
            setOnClickListener { expand() }
        }

        workoutDescriptionView.visibility = View.VISIBLE
        chipGroup.visibility = View.VISIBLE

        exercisesView.visibility = View.GONE
        logWorkoutBtn.visibility = View.GONE
        addExerciseBtn.visibility = View.GONE
    }

    fun expand() {
        expanded = false

        chevron.apply {
            setImageResource(R.drawable.ic_chevron_up)
            setOnClickListener { collapse() }
        }

        workoutDescriptionView.visibility = View.GONE
        chipGroup.visibility = View.GONE

        exercisesView.visibility = View.VISIBLE
        logWorkoutBtn.visibility = View.VISIBLE
        addExerciseBtn.visibility = View.VISIBLE
    }

    private fun showAddExercisesDialog() {

        val frag = AddExerciseDialogFragment.newInstance().apply {
            setOnDialogShownListener(object : AddExerciseDialogFragment.OnDialogShownListener {
                override fun onDialogShown(dialog: AddExerciseDialogFragment) {
                    workout?.exercises?.let {
                        dialog.setCheckedExercises(it)
                    }
                }
            })

            setOnSaveClickedListener { exercises ->
                val fieldsToUpdate = HashMap<WorkoutField, Any?>().apply {
                    put(WorkoutField.EXERCISES, exercises)
                }
                val uidRef = workout?.uid
                if (uidRef == null) {
                    Log.w("Update workout", "Cannot update a workout without a UID!!")
                    return@setOnSaveClickedListener null
                }

                FitApplication.instance.workoutManager.receiver.updateWorkout(uidRef, fieldsToUpdate, object : TaskListener<Unit?> {
                    override fun onSuccess(value: Unit?) {
                        val exercisesRef = workout?.exercises
                        if (exercisesRef == null) {
                            workout?.exercises = ArrayList(exercises)
                        } else {
                            exercisesRef.apply {
                                clear()
                                addAll(exercises)
                                exerciseAdapter?.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onFailure(e: Exception?) {
                        Log.w("Update workout", "Could not update exercises for workout!")
                        Log.w("Update workout", e?.message.toString())
                    }
                })
            }
        }

        getActivity(context)?.let { activity ->
            frag.show(activity.supportFragmentManager, "dialog")
        }
    }

    private fun setDescription(desc: String?) {
        workoutDescriptionView.text = if (desc.isNullOrBlank()) "No description" else desc
    }

    override fun setTitle(title: String) {
        workoutTitleView.text = title
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder != null) {
            touchHelper?.startDrag(viewHolder)
        }
    }

    override fun onEndDrag(viewHolder: RecyclerView.ViewHolder?) {
        // Do nothing yet
    }


    fun setWorkout(workout: Workout) {
        this.workout = workout

        setTitle(workout.name)
        setDescription(workout.description)
        updateWorkoutImage(workout.imageBmp)
        updateLabels(workout.targetMuscleGroups?.map { it.name })
        updateExercises(workout.exercises)
    }

    private fun updateWorkoutImage(drawable: Drawable?) {
        if (drawable == null) {
            workoutImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.default_workout_img, null))
        } else {
            workoutImageView.setImageDrawable(drawable)
        }
    }

    private fun updateWorkoutImage(bmp: Bitmap?) {
        if (bmp == null) {
            workoutImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.default_workout_img, null))
        } else {
            workoutImageView.setImageDrawable(BitmapDrawable(resources, bmp))
        }
    }

    private fun updateLabels(labels: List<String>?) {
        chipGroup.removeAllViews()
        val layoutInflater = LayoutInflater.from(context)

        labels ?: return

        lateinit var chip: Chip
        for (label in labels) {
            chip = layoutInflater.inflate(R.layout.action_chip_layout, chipGroup, false) as Chip
            chip.apply {
                text = label
                isClickable = false
                isFocusable = false
            }
            chipGroup.addView(chip)
        }
    }

    private fun updateExercises(exercises: MutableList<Exercise>) {
        if (exerciseAdapter == null) {
            exerciseAdapter = ExerciseAdapter(exercises, this)
        } else {
            exerciseAdapter!!.setExercises(exercises)
        }
    }

    private fun getActivity(context: Context?): AppCompatActivity? {
        if (context == null) {
            return null
        } else if (context is ContextWrapper) {
            return if (context is AppCompatActivity) {
                context
            } else {
                getActivity(context.baseContext)
            }
        }
        return null
    }
}