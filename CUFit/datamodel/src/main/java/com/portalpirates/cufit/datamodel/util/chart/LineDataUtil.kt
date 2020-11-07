package com.portalpirates.cufit.datamodel.util.chart

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

object LineDataUtil {

    private fun <T : Number> toEntries(map: Map<T, T>): List<Entry> {
        return map.map { mapEntry ->
            Entry(mapEntry.key.toFloat(), mapEntry.value.toFloat())
        }
    }

    fun <T : Number> toLineDataSet(map: Map<T, T>, label: String): LineDataSet {
        return LineDataSet(toEntries(map), label)
    }

    fun <T : Number> toLineData(map: Map<T, T>, label: String): LineData {
        return LineData(LineDataSet(toEntries(map), label))
    }

}