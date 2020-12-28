package com.ethanprentice.fitter.ui.view

import android.content.Context
import android.util.AttributeSet

// creating this so i remember to make it - but not gonna do it rn cuz it'll take awhile
// this way we can handle conversions and displaying the unit suffix in the view instead of some weird
// formatter before we set the text here
class MeasuredTextView(context: Context, attrs: AttributeSet?, defStyle: Int) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)



}