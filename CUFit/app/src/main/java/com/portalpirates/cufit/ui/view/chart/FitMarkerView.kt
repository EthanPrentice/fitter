package com.portalpirates.cufit.ui.view.chart

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.portalpirates.cufit.R


class FitMarkerView(context: Context) : MarkerView(context, R.layout.chart_marker) {

    private var markerText: TextView? = findViewById(R.id.marker_text)
    private var mOffset: MPPointF? = null

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        markerText!!.text = "${e.y}"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF? {
        if (mOffset == null) {
            mOffset = MPPointF(-(width / 2).toFloat(), -1.25f*height.toFloat())
        }
        return mOffset
    }

    override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
        val offset = getOffsetForDrawingAtPoint(posX, posY)

        val saveId = canvas!!.save()

        if (posX + offset.x + width > canvas.width) {
            offset.x -= (posX + offset.x + width) - canvas.width
        }

        canvas.translate(posX + offset.x, posY + offset.y)
        draw(canvas)
        canvas.restoreToCount(saveId)
    }

}