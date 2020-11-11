package com.portalpirates.cufit.datamodel.data.graph


import android.graphics.drawable.Drawable
import com.portalpirates.cufit.datamodel.data.exercise.Workout
//import com.portalpirates.cufit.datamodel.data.exercise.Exercise
//import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import java.util.*



class LineDataConfig(
        var startDate: Date,
        var endDate: Date,
        var label: String? = null,
        var movingAverageDays: Int

) {

    init {
        movingAverageDays = 1
    }

}



