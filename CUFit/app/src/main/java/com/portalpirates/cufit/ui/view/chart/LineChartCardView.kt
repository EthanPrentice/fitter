package com.portalpirates.cufit.ui.view.chart

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.view.FitCardView

class LineChartCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val chartView = FitLineChart(context, null, 0)

    var isStatusEnabled: Boolean = true
        set(value) {
            topBarVisible = value
            field = value
        }

    var isIncreaseGood: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                updateStatus()
            } else {
                field = value
            }
        }

    init {
        chartView.clipToOutline = true
        setContentView(chartView, 0)
        topBarVisible = true
        setStatusIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_up, null))
        statusColor = ContextCompat.getColor(context, R.color.status_green)
    }

    /**
     * Limits the view to use only one data set
     */
    fun setData(data: LineDataSet) {
        chartView.data = LineData(data)
        if (isStatusEnabled) {
            updateStatus()
        }
    }

    fun updateStatus() {
        val firstVal = chartView.data.dataSets[0].getEntriesForXValue(chartView.data.xMin)[0].y
        val lastVal = chartView.data.dataSets[0].getEntriesForXValue(chartView.data.xMax)[0].y

        val diff = lastVal - firstVal
        setStatusText(diff.toString())

        statusColor = if (isIncreaseGood && diff >= 0 || !isIncreaseGood && diff <= 0) {
            ContextCompat.getColor(context, R.color.status_green)
        } else {
            ContextCompat.getColor(context, R.color.status_red)
        }

        if (diff >= 0) {
            setStatusIcon(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up))
        } else {
            setStatusIcon(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        chartView.layoutParams = chartView.layoutParams.apply {
            height = measuredHeight - topBarLayout.measuredHeight
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}