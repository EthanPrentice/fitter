package com.ethanprentice.fitter.datamodel.data.util

import android.graphics.drawable.Drawable

interface SwimlaneItem {
    fun getTitle(): String
    fun getDrawable(): Drawable?
}