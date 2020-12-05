package com.portalpirates.cufit.ui.progress.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.LineDataSet
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.view.chart.LineChartCardView

class ProgressCard(context: Context, attrs: AttributeSet?, defStyle: Int) : LineChartCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    val toggleTextView: TextView
    var togglePos = 0

    init {
        toggleTextView = TextView(context).apply {
            setTextAppearance(R.style.subtitle)
            setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            togglePos = 0
            text = "(${EXERCISES[togglePos]})"

            setOnClickListener {
                onTextViewClicked()
            }
        }
        addTextViewToTitle(toggleTextView)
    }

    private fun addTextViewToTitle(tv: TextView) {
        tv.id = View.generateViewId()
        val params = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            leftToRight = R.id.title
            topToTop = R.id.title
            bottomToBottom = R.id.title
            rightToLeft = R.id.status
            horizontalBias = 0f

            val leftMargin = resources.getDimensionPixelOffset(R.dimen.LU_1)
            setMargins(leftMargin, 0, 0, 0)
        }
        topBarLayout.addView(tv, params)
    }

    private fun getNextExercisePos(pos: Int): Int {
        return (pos + 1) % EXERCISES.size
    }

    private fun onTextViewClicked() {
        val nextPos = getNextExercisePos(togglePos)

        val userUid = FitApplication.instance.userManager.provider.getFirebaseUser()!!.uid
        FitApplication.instance.workoutManager.provider.getExerciseDataSet(userUid, EXERCISES[nextPos], null, object : TaskListener<LineDataSet?> {
            override fun onSuccess(value: LineDataSet?) {
                setData(value)
                togglePos = nextPos
                toggleTextView.text = "(${EXERCISES[togglePos]})"
            }

            override fun onFailure(e: Exception?) {
                Log.w("Progress", "Could not retrieve data for exercise!")
            }
        })
    }


    companion object {
        val EXERCISES = listOf(
            "Bench Press",
            "Deadlift",
            "Squats",
            "OHP"
        )
    }
}