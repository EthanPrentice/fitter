package com.portalpirates.cufit.ui.workout.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.workout.Exercise

class AddExerciseView(context: Context, attrs: AttributeSet?, defStyle: Int) : ConstraintLayout(context, attrs, defStyle), Checkable {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val titleView: TextView
    private val chipGroup: ChipGroup
    private val checkView: ImageView

    private var checked = false
    private var onCheckedChangeListener: ((AddExerciseView, Boolean) -> Unit?)? = null

    var exercise: Exercise? = null
        set(value) {
            field = value

            titleView.text = value?.name

            chipGroup.removeAllViews()
            val layoutInflater = LayoutInflater.from(context)
            value?.muscleGroups?.forEach { muscleGroup ->
                val chip = layoutInflater.inflate(R.layout.action_chip_layout, chipGroup, false) as Chip
                chip.apply {
                    text = muscleGroup.name
                    isClickable = false
                    isFocusable = false
                    isEnabled = false
                }
                chipGroup.addView(chip)
            }
        }


    init {
        val v = inflate(context, R.layout.add_exercise_entry, this)

        titleView = v.findViewById(R.id.exercise_name)
        chipGroup = v.findViewById(R.id.chip_group)
        checkView = v.findViewById(R.id.check)

        setOnClickListener {
            toggle()
        }
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun toggle() {
        setChecked(!checked)
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked == checked) return

        this.checked = checked
        checkView.visibility = if (checked) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        onCheckedChangeListener?.invoke(this, checked)
    }

    fun setOnCheckedChangeListener(listener: ((AddExerciseView, Boolean) -> Unit?)?) {
        onCheckedChangeListener = listener
    }
}