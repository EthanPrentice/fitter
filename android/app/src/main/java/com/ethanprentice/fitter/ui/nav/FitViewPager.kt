package com.ethanprentice.fitter.ui.nav

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.viewpager.widget.ViewPager
import java.lang.Integer.max

class FitViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    constructor(context: Context) : this(context, null)

    init {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                minimumHeight = (parent as ViewGroup).height
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightSpec = heightMeasureSpec
        var height = 0

        var child = getCurrentChild()
        if (child != null) {
            child.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            height = child.measuredHeight
        }
        else {
            for (i in 0 until childCount) {
                child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                height = max(height, child.measuredHeight)
            }
        }

        height = max(height, minimumHeight)

        if (height != 0) {
            newHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    fun getCurrentChild(): View? {
        val currFrag = (adapter as? ViewPagerAdapter)?.currFrag
        return currFrag?.rootView
    }

}