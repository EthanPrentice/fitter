package com.ethanprentice.fitter.ui.view.chart

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.ui.view.FitCardView


class FitLineChart(context: Context, attrs: AttributeSet?, defStyle: Int) : LineChart(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.FitLineChart, defStyle, 0)
        showAxisLabels(typedArr.getBoolean(R.styleable.FitLineChart_showAxisLabels, false))
        typedArr.recycle()

        legend.isEnabled = false
        description.isEnabled = false
        setDrawBorders(false)

        formatXAxis()
        formatYAxis()

        configureChartInteraction()

        marker = FitMarkerView(context)

        setViewPortOffsets(0f, 0f, 0f, 0f)
    }

    private fun configureChartInteraction() {
        isHighlightPerDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        isDoubleTapToZoomEnabled = false
        isHighlightPerTapEnabled = true
    }

    override fun setData(data: LineData?) {
        data?.dataSets?.forEach { dataSet ->
            (dataSet as? LineDataSet)?.let {
                formatLineDataSet(it)
            }
        }
        super.setData(data)
        // Axis has at least 33% height buffer below lowest point
        axisLeft.axisMinimum = yMin - 0.33f * (yMax - yMin)
    }

    private fun formatLineDataSet(dataSet: LineDataSet) {
        dataSet.apply {
            color = ContextCompat.getColor(context, R.color.chart_primary)
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(context, R.color.chart_secondary)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawValues(false)
            setDrawCircles(true)

            circleRadius = resources.getDimension(R.dimen.LU_0_5)
            setCircleColor(this@FitLineChart.getCircleColor())
            circleHoleRadius = resources.getDimension(R.dimen.LU_0_5) * 7/10f
            circleHoleColor = ContextCompat.getColor(context, R.color.chart_primary)

            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
        }
    }

    private fun formatXAxis() {
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = ContextCompat.getColor(context, R.color.text_secondary)
        xAxis.setDrawGridLines(false)
    }

    private fun formatYAxis() {
        axisRight.setDrawLabels(false)
        axisRight.setDrawGridLines(false)
        axisRight.setDrawAxisLine(false)

        axisLeft.textSize = 10f
        axisLeft.textColor = ContextCompat.getColor(context, R.color.text_secondary)
        axisLeft.setDrawGridLines(false)
    }

    fun showAxisLabels(enabled: Boolean) {
        xAxis.setDrawLabels(enabled)
        xAxis.setDrawAxisLine(enabled)
        axisLeft.setDrawLabels(enabled)
        axisLeft.setDrawAxisLine(enabled)
    }

    private fun getCircleColor(): Int {
        return (parent.parent as? FitCardView)?.cardBackgroundColor?.defaultColor ?:
                ContextCompat.getColor(context, R.color.background)
    }
}