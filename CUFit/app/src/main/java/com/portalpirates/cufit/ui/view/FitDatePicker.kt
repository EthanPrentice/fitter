package com.portalpirates.cufit.ui.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import java.util.*


class FitDatePicker(context: Context, attrs: AttributeSet?, defStyle: Int) : FitEditText(context, attrs, defStyle), OnDateSetListener {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // If there's already an existing DatePicker then dismiss it's dialog before re-assigning the value
    private var pickerDialog: DatePickerDialog? = null
        set(value) {
            field?.dismiss()
            field = value
        }

    /**
     * When date is set, update editText
     */
    var date: Date? = null
        set(value) {
            field = value
            value ?: return

            val dateStr = SimpleDateFormat("MMMM d, YYYY", resources.configuration.locales.get(0)).format(value.time)
            editText.setText(dateStr)
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_clear), null)
        }


    val dateSelected: Boolean
        get() = date != null

    init {
        isClickable = true
        isFocusable = true

        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_date_range), null)
    }

    /**
     * Don't send touch events to children - handle them here so if we press an editText or some subview in
     * TextInputLayout it get's handled here instead
     */
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (isTouchEventInRightDrawable(event)) {
                if (dateSelected) {
                    onClearPressed()
                    return true
                }
            }

            showDatePicker()
            performClick()
        }
        return true
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        cal.isLenient = false
        pickerDialog = DatePickerDialog(context, this, year, month, day).apply {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    private fun isTouchEventInRightDrawable(event: MotionEvent): Boolean {
        val textLocation = IntArray(2)
        editText.getLocationOnScreen(textLocation)

        return event.rawX >= textLocation[0] + editText.measuredWidth - editText.totalPaddingRight
                && event.rawX <= textLocation[0] + editText.measuredWidth - editText.paddingRight
    }

    private fun onClearPressed() {
        date = null
        editText.text?.clear()
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_date_range), null)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)

        date = Date(cal.timeInMillis)
    }
}