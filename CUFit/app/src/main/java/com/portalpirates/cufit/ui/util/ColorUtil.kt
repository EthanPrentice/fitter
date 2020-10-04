package com.portalpirates.cufit.ui.util

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R

object ColorUtil {

    private val invertibleColors = mapOf(
        R.color.component_bg to R.color.component_bg_inverse,
        R.color.component_bg_secondary to R.color.component_bg_secondary_inverse,
        R.color.component_bg_tertiary to R.color.component_bg_tertiary_inverse,
        R.color.component_fg to R.color.component_fg_inverse,
        R.color.component_fg_secondary to R.color.component_fg_secondary_inverse,
        R.color.overlay_dark to R.color.overlay_light,
        R.color.overlay_light to R.color.overlay_dark
    )


    fun getSingleColorStateList(color: Int): ColorStateList {
        val states: Array<IntArray> = arrayOf(IntArray(0))
        val colors = IntArray(1) { color }
        return ColorStateList(states, colors)
    }

    fun getInvertibleColor(context: Context, id: Int, inverted: Boolean): Int {
        return if (!inverted) {
            ContextCompat.getColor(context, id)
        } else {
            val invertedId = invertibleColors[id] ?: id
            ContextCompat.getColor(context, invertedId)
        }
    }
}