package com.ethanprentice.fitter.ui.util

import android.graphics.Bitmap

interface ImageSelector {
    fun onSelected(bmp: Bitmap?)
}