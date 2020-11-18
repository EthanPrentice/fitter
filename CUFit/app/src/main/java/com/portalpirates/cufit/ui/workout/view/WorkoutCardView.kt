package com.portalpirates.cufit.ui.workout.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.ui.view.FitCardView


class WorkoutCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var workout: Workout? = null
        private set

    private val workoutTitleView: TextView
    private val workoutDescriptionView: TextView
    private val workoutOwnerImageView: ImageView

    private val chipGroup: ChipGroup

    init {
        val inflater = LayoutInflater.from(context)
        val content = inflater.inflate(R.layout.workout_card_layout, rootView, false)

        workoutTitleView = content.findViewById(R.id.workout_title)
        workoutDescriptionView = content.findViewById(R.id.workout_desc)
        workoutOwnerImageView = content.findViewById(R.id.workout_owner_img)
        chipGroup = content.findViewById(R.id.chip_group)

        setTitle("Title")
        setDescription("Hello world!  This is an example description.")
        updateLabels(listOf("Label 1", "Label 2", "Label 3"))
        updateOwnerImage(null)

        setContentView(content)
    }

    fun setWorkout(workout: Workout) {
        // TODO: actually do stuff here
    }

    private fun updateOwnerImage(drawable: Drawable?) {
        if (drawable == null) {
            workoutOwnerImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.default_avatar, null))
        } else {
            workoutOwnerImageView.setImageDrawable(drawable)
        }
    }

    private fun updateLabels(labels: List<String>) {
        chipGroup.removeAllViews()
        val layoutInflater = LayoutInflater.from(context)
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

    private fun setDescription(desc: String) {
        workoutDescriptionView.text = desc
    }

    override fun setTitle(title: String) {
        workoutTitleView.text = title
    }

    // TODO: DELETE ME
    fun setFakeOwnerBmp(ownerBmp: Bitmap?) {
        if (ownerBmp == null) {
            updateOwnerImage(null)
        } else {
            updateOwnerImage(BitmapDrawable(resources, ownerBmp))
        }
    }

    fun setFakeLabels(labels: List<String>) = updateLabels(labels)

    fun setFakeDescription(str: String) = setDescription(str)

}