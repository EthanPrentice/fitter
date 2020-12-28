package com.ethanprentice.fitter.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView


class LockableNestedScrollView(context: Context, attrs: AttributeSet?, defStyle: Int) : NestedScrollView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // by default is scrollable
    var scrollingEnabled = true

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return scrollingEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return scrollingEnabled && super.onInterceptTouchEvent(ev)
    }
}