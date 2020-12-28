package com.ethanprentice.fitter.ui.workout.view

import android.content.Context
import android.icu.util.MeasureUnit
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.measure.Weight
import com.ethanprentice.fitter.datamodel.data.workout.Exercise
import com.ethanprentice.fitter.ui.view.MeasuredEditText
import java.util.*

class ExerciseView(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    var exercise: Exercise
        get() {
            fun getNum(str: String?): Int = if (str.isNullOrBlank()) 0 else str.toInt()
            val sets = getNum(setsInput.text.toString())
            val reps = getNum(repsInput.text.toString())

            return Exercise(
                titleView.text.toString(),
                repWeightView.getMeasureFromEditText(::Weight),
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

    private var onFieldChangedListener: OnFieldChangedListener? = null

    init {
        inflate(context, R.layout.exercise_view, this)

        titleView = findViewById(R.id.exercise_name)
        setsInput = findViewById(R.id.sets_input)
        repsInput = findViewById(R.id.rep_input)
        repWeightView = findViewById(R.id.rep_weight)

        repWeightView.measureUnit = MeasureUnit.POUND
        repWeightView.decimals = 0

        formatEditText(setsInput) {
            onFieldChangedListener?.onSetsChanged(exercise.sets)
        }
        formatEditText(repsInput) {
            onFieldChangedListener?.onRepsChanged(exercise.reps)
        }
        formatEditText(repWeightView.editText!!) {
            onFieldChangedListener?.onWeightChanged(exercise.weight ?: Weight(0, Date()))
        }

        repWeightView.editText?.filters = arrayOf(InputFilter.LengthFilter(3))
        repWeightView?.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
    }

    private fun formatEditText(editText: EditText?, onTextChanged: (() -> Unit?)?) {
        if (editText == null) return
        editText.setTextAppearance(R.style.subtitle)
        editText.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
        editText.doOnTextChanged { _, _, _, _ ->
            onTextChanged?.invoke()
        }
    }

    fun setOnFieldChangeListener(listener: OnFieldChangedListener) {
        onFieldChangedListener = listener
    }


    interface OnFieldChangedListener {
        fun onRepsChanged(reps: Int)
        fun onSetsChanged(sets: Int)
        fun onWeightChanged(weight: Weight)
    }

}