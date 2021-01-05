package com.ethanprentice.fitter.viewmodel.util

import android.graphics.Bitmap

interface ImageSelector {
    fun onSelected(bmp: Bitmap?)
}