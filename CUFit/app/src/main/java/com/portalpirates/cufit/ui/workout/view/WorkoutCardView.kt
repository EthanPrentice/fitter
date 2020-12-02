package com.portalpirates.cufit.ui.workout.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.icu.util.MeasureUnit
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.ui.util.DragEventListener
import com.portalpirates.cufit.ui.util.item_touch.SimpleItemTouchHelperCallback
import com.portalpirates.cufit.ui.view.FitButton
import com.portalpirates.cufit.ui.view.FitCardView
import com.portalpirates.cufit.ui.workout.ExerciseAdapter
import kotlinx.android.synthetic.main.button_layout.view.*
import java.util.*
import kotlin.collections.ArrayList


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
    private var addWorkoutBtn: FitButton

    private val exercises = ArrayList<Exercise>()
    private val exerciseAdapter = ExerciseAdapter(exercises, this)
    private val touchHelper = ItemTouchHelper(SimpleItemTouchHelperCallback(exerciseAdapter))


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
            adapter = exerciseAdapter
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically() = false
            }

            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
        touchHelper.attachToRecyclerView(exercisesView)

        exercises.add(Exercise("Bench Press", Weight(225, MeasureUnit.POUND, Date()), 5, 5))
        exercises.add(Exercise("Tricep Extensions", Weight(55, MeasureUnit.POUND, Date()), 5, 6))
        exercises.add(Exercise("Pec Flies", Weight(180, MeasureUnit.POUND, Date()), 3, 8))
        exercises.add(Exercise("Squeeze Press", Weight(55, MeasureUnit.POUND, Date()), 4, 8))

        logWorkoutBtn = content.findViewById(R.id.log_workout_btn)
        addWorkoutBtn = content.findViewById(R.id.add_workout_btn)
        // override text style
        logWorkoutBtn.btn_text_view.setTextAppearance(R.style.body_text)
        addWorkoutBtn.btn_text_view.setTextAppearance(R.style.body_text)

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
        addWorkoutBtn.visibility = View.GONE
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
        addWorkoutBtn.visibility = View.VISIBLE
    }

    private fun setDescription(desc: String?) {
        workoutDescriptionView.text = desc
    }

    override fun setTitle(title: String) {
        workoutTitleView.text = title
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder != null) {
            touchHelper.startDrag(viewHolder)
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
            chip = layoutInflater.inflate(R.layout.single_chip_layout, chipGroup, false) as Chip
            chip.apply {
                text = label
                isClickable = false
                isFocusable = false
            }
            chipGroup.addView(chip)
        }
    }
}