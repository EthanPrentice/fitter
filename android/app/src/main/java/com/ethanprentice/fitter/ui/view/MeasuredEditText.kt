package com.ethanprentice.fitter.ui.view

import android.content.Context
import android.icu.text.MeasureFormat
import android.icu.text.NumberFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.measure.FitMeasure
import com.ethanprentice.fitter.datamodel.data.measure.Height
import com.ethanprentice.fitter.datamodel.data.measure.MeasureConverter
import com.ethanprentice.fitter.datamodel.data.measure.Weight
import java.util.*


class MeasuredEditText(context: Context, attrs: AttributeSet?, defStyle: Int) : FitEditText(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var decimals: Int = DEFAULT_DECIMALS

    /**
     * The backing measure of the EditText, setter handles conversions, locales and the cursor handling that comes with each
     */
    var measure: FitMeasure? = null
       set(value) {
           if (value == field) {
               field = value
               return
           }

           // if null, clear text but keep unit
           if (value == null) {
               editText.setText("")
               field = value
               return
           }

           val convertedMeasure = getConvertedMeasure(value) ?: return

           val formatter = NumberFormat.getInstance(resources.configuration.locales[0])
           val text = formatter.format(convertedMeasure.number)

           // Handle the adding of locale characters for the cursor
           val deltaNonNumbers = text.length - (editText.text?.length ?: 0)
           val selStart = editText.selectionStart

           editText.setText(text)

           val cursorPos = selStart + deltaNonNumbers
           editText.setSelection(cursorPos)

           field = convertedMeasure
       }

    // separate from measure.unit since we want units to persist if measure gets set to null
    var measureUnit: MeasureUnit? = null
        set(value) {
            suffixText = if (value != null) {
                " " + getUnitText(value)
            } else {
                ""
            }
            field = value
        }

    init {
        editText.hintTextColors?.let {
            setSuffixTextColor(it)
        }

        val typedArr = context.obtainStyledAttributes(attrs, R.styleable.MeasuredEditText, defStyle, 0)
        decimals = typedArr.getInt(R.styleable.MeasuredEditText_decimals, DEFAULT_DECIMALS)
        typedArr.recycle()
    }

    /**
     * Converts the parameter to [measureUnit] if possible, and handles rounding according to [decimals]
     * @return [otherMeasure] converted to [measureUnit] or null if the conversion was unsuccessful
     */
    private fun getConvertedMeasure(otherMeasure: FitMeasure): FitMeasure? {
        return if (measureUnit == null) {
            null
        } else {
            try {
                val factory: ((Number, MeasureUnit?, Date) -> FitMeasure) = when (otherMeasure) {
                    is Weight -> ::Weight
                    is Height -> ::Height
                    else -> throw IllegalArgumentException()
                }
                MeasureConverter.convert(otherMeasure, measureUnit!!, decimals, factory)
            } catch(e: IllegalArgumentException) {
                Log.e(MeasureConverter.TAG, e.message.toString())
                null
            }
        }
    }

    private fun getUnitText(unit: MeasureUnit): String {
        val formatter = MeasureFormat.getInstance(resources.configuration.locales[0],
            MeasureFormat.FormatWidth.SHORT
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            formatter.getUnitDisplayName(unit)
        } else {
            // kind of a hack, but we have no API call for API < 28
            formatter.format(Measure(0, unit)).removePrefix("0").trim()
        }
    }

    fun <T : FitMeasure> getMeasureFromEditText(factory: (Number, MeasureUnit?, Date) -> T): T {
        val currTime = Date()
        editText.text?.let {
            if (!it.isBlank()) {
                val number = NumberFormat.getInstance().parse(it.toString())
                return factory(number, measureUnit, currTime)
            }
        }
        return factory(0, measureUnit, currTime)
    }

    companion object {
        private const val DEFAULT_DECIMALS = 2
    }
}