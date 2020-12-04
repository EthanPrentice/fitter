package com.portalpirates.cufit.ui.workout.view

import android.content.Context
import android.icu.util.MeasureUnit
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.ui.view.MeasuredEditText
import java.util.*

class ExerciseView(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    var exercise: Exercise
        get() {
            val sets = setsInput.text.toString().toInt()
            val reps = repsInput.text.toString().toInt()

            return Exercise(
                titleView.text.toString(),
                repWeightView.measure as Weight,
                sets, reps
            )
        }
        set(value) {
            titleView.text = value.name
            setsInput.setText(value.sets.toString())
            repsInput.setText(value.reps.toString())
            repWeightView.measure = value.weight
        }

    private val titleView: TextView
    private val setsInput: EditText
    private val repsInput: EditText
    private val repWeightView: MeasuredEditText

    init {
        inflate(context, R.layout.exercise_view, this)

        titleView = findViewById(R.id.exercise_name)
        setsInput = findViewById(R.id.sets_input)
        repsInput = findViewById(R.id.rep_input)
        repWeightView = findViewById(R.id.rep_weight)

        repWeightView.measureUnit = MeasureUnit.POUND
        repWeightView.decimals = 0

        formatEditText(setsInput)
        formatEditText(repsInput)
        formatEditText(repWeightView.editText)

        repWeightView.editText?.filters = arrayOf(InputFilter.LengthFilter(3))
        repWeightView?.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
    }

    private fun formatEditText(editText: EditText?) {
        if (editText == null) return
        editText.setTextAppearance(R.style.subtitle)
        editText.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
    }

}