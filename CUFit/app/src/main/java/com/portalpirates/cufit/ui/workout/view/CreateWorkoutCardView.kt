package com.portalpirates.cufit.ui.workout.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.workout.MuscleGroup
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.datamodel.data.workout.WorkoutBuilder
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.util.ImageSelectorLock
import com.portalpirates.cufit.ui.view.ChooseImageButton
import com.portalpirates.cufit.ui.view.FitButton
import com.portalpirates.cufit.ui.view.FitCardView


class CreateWorkoutCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val chooseImageBtn: ChooseImageButton
    private var imageBmp: Bitmap? = null

    private val workoutTitleView: TextInputLayout
    private val workoutDescView: TextInputLayout

    private val chipGroup: ChipGroup
    private var muscleGroups: List<MuscleGroup>? = null

    private val saveBtn: FitButton
    private val cancelBtn: FitButton


    private var onCreateListener: TaskListener<Workout> = object : TaskListener<Workout> {
        override fun onSuccess(value: Workout) { }
        override fun onFailure(e: Exception?) { }
    }
    private var onCancelListener: (() -> Unit?)? = null


    init {
        val inflater = LayoutInflater.from(context)
        val content = inflater.inflate(R.layout.create_workout_card_layout, rootView, false)

        chooseImageBtn = content.findViewById<ChooseImageButton>(R.id.choose_photo_btn).apply {
            setOnImageSelectedListener { bmp ->
                imageBmp = bmp
                Unit
            }
            setOnClearListener {
                imageBmp = null
            }
        }

        workoutTitleView = content.findViewById(R.id.workout_title)
        workoutDescView = content.findViewById(R.id.workout_desc)

        fun EditText.initEditText() {
            setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            setHintTextColor(ContextCompat.getColor(context, R.color.text_secondary))
        }

        workoutTitleView.editText?.apply {
            setTextAppearance(R.style.body_text)
            initEditText()
        }
        workoutDescView.editText?.apply {
            setTextAppearance(R.style.subtitle)
            initEditText()
        }


        chipGroup = content.findViewById(R.id.chip_group)

        saveBtn = content.findViewById<FitButton>(R.id.save_btn).apply {
            setOnClickListener {
                saveWorkout()
            }
        }

        cancelBtn = content.findViewById<FitButton>(R.id.cancel_btn).apply {
            setOnClickListener {
                onCancelListener?.invoke()
            }
        }

        setContentView(content)
    }

    private fun saveWorkout() {
        val ownerUid = FitApplication.instance.userManager.provider.getFirebaseUser()?.uid
        if (ownerUid == null) {
            Log.w("Create Workout", "Unable to create workout, no authenticated user")
            Toast.makeText(context, "Unable to create workout.  Please log in to the app again.", Toast.LENGTH_LONG).show()
            return
        }

        val name = workoutTitleView.editText?.text?.toString()
        if (name.isNullOrBlank()) {
            Toast.makeText(context, "Please enter a title", Toast.LENGTH_LONG).show()
            return
        }

        val muscleGroups = chipGroup.checkedChipIds.map { viewId ->
            val chipText = chipGroup.findViewById<Chip>(viewId).text.toString()
            MuscleGroup(chipText)
        }

        val builder = WorkoutBuilder().setName(name)
            .setOwnerUid(ownerUid)
            .setDescription(workoutDescView.editText?.text?.toString())
            .setImage(imageBmp)
            .setPublic(false)
            .setTargetMuscleGroups(muscleGroups)


        FitApplication.instance.workoutManager.receiver.createWorkout(builder, onCreateListener)
    }

    fun clearData() {
        workoutTitleView.editText?.text?.clear()
        workoutDescView.editText?.text?.clear()
        chooseImageBtn.setImageBitmap(null)
        imageBmp = null

        chipGroup.checkedChipIds.forEach { viewId ->
            chipGroup.findViewById<Chip>(viewId).apply {
                isSelected = false
                isCheckable = false
            }
        }
    }

    fun setOnCreateTaskListener(listener: TaskListener<Workout>) {
        onCreateListener = listener
    }

    fun setOnCancelListener(listener: (() -> Unit?)?) {
        onCancelListener = listener
    }

    fun populateMuscleGroups(muscleGroups: List<MuscleGroup>) {
        this.muscleGroups = muscleGroups

        val layoutInflater = LayoutInflater.from(context)
        muscleGroups.forEach { muscleGroup ->
            val chip = layoutInflater.inflate(R.layout.filter_chip_layout, chipGroup, false) as Chip
            chip.apply {
                text = muscleGroup.name
                isClickable = true
                isFocusable = true
                isCheckable = true
            }
            chipGroup.addView(chip)
        }
    }

    fun assignLock(lock: ImageSelectorLock) {
        chooseImageBtn.assignLock(lock)
    }

}