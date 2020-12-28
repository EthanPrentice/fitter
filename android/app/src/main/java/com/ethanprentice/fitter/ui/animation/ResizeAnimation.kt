package com.ethanprentice.fitter.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

class ResizeAnimation(private val view: View, private val target: Float, private val start: Float, private val mode: Mode) : Animation() {
    constructor(view: View, target: Int, start: Int, mode: Mode) : this(view, target.toFloat(), start.toFloat(), mode)

    enum class Mode {
        WIDTH,
        HEIGHT,
        WEIGHT
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val newValue = (start + (target - start) * interpolatedTime)

        when (mode) {
            Mode.WIDTH -> view.layoutParams.width = newValue.toInt()
            Mode.HEIGHT -> view.layoutParams.height = newValue.toInt()
            Mode.WEIGHT -> (view.layoutParams as LinearLayout.LayoutParams).weight = newValue
        }
        view.parent.requestLayout()
    }

    override fun willChangeBounds() = true
}