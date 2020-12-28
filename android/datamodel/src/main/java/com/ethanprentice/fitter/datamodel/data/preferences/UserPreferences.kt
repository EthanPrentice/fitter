package com.ethanprentice.fitter.datamodel.data.preferences

import android.icu.util.MeasureUnit
import android.util.Log
import com.ethanprentice.fitter.datamodel.data.measure.Height
import com.ethanprentice.fitter.datamodel.data.measure.Weight

class UserPreferences(_weightUnit: MeasureUnit, _heightUnit: MeasureUnit) : Preferences() {

    var weightUnit = _weightUnit
        set(value) {
            if (value.type != Weight.type) {
                Log.e(TAG, "Cannot assign the user's weight units to $value")
            } else {
                field = value
            }
        }

    var heightUnit = _heightUnit
        set(value) {
            if (value.type != Height.type) {
                Log.e(TAG, "Cannot assign the user's weight units to $value")
            } else {
                field = value
            }
        }

    init {
        // If the values are invalid then the heightUnit and weightUnit will not be set.
        // If this is the first time we instantiate this object and they are invalid, assign them to be
        //    the relevant base units instead.
        if (_weightUnit.type != Weight.type) {
            weightUnit = Weight.BASE_UNIT
        }
        if (_heightUnit.type != Height.type) {
            heightUnit = Height.BASE_UNIT
        }
    }


    companion object {
        private const val TAG = "UserPreferences"
    }


}