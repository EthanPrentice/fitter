package com.portalpirates.cufit.ui.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.ui.FitActivity.Companion.RESULT_LOAD_IMAGE
import com.portalpirates.cufit.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


class ChooseImageButton(context: Context, attrs: AttributeSet?, defStyle: Int) : RelativeLayout(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val imageView = CircleImageView(context)
    private val rippleView = View(context)
    val editBtn = FitButton(context)

    init {
        makeClickable()

        imageView.transitionName = "$transitionName:imageView"
        editBtn.transitionName = "$transitionName:editBtn"

        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.add_photo))
        imageView.borderWidth = resources.getDimensionPixelOffset(R.dimen.LU_0_5)
        imageView.borderColor = ContextCompat.getColor(context, R.color.icon_primary)

        rippleView.setBackgroundResource(R.drawable.circular_ripple)

        editBtn.icon = ContextCompat.getDrawable(context, R.drawable.ic_add)
        editBtn.setOnClickListener {
            setImageBitmap(null)
        }

        val photoParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val editBtnParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addView(imageView, photoParams)
        addView(rippleView, photoParams)
        addView(editBtn, editBtnParams)
    }

    fun selectPhotoFromGallery(activity: Activity) {
        val gallery = Intent(Intent.ACTION_GET_CONTENT)
        gallery.type = "image/*"

        startActivityForResult(activity, gallery, RESULT_LOAD_IMAGE, null)
    }

    fun setImageBitmap(bmp: Bitmap?) {
        if (bmp == null) {
            imageView.setImageResource(R.drawable.add_photo)
            switchToEditMode()
        }
        else {
            imageView.setImageBitmap(bmp)
            switchToClearMode()
        }
    }

    fun setOnClearListener(l: ((v: View) -> Unit)?) {
        editBtn.setOnClickListener {
            setImageBitmap(null)
            l?.invoke(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Check if the event is within the circular bounds of the photoView
        val photoViewRadius = imageView.measuredWidth / 2f
        val photoViewMiddleX = imageView.x + imageView.measuredWidth / 2f
        val photoViewMiddleY = imageView.y + imageView.measuredHeight / 2f

        val distanceX = abs(event.x - photoViewMiddleX)
        val distanceY = abs(event.y - photoViewMiddleY)

        val distance = sqrt(distanceX.pow(2) + distanceY.pow(2))
        val pressedPhotoView = distance <= photoViewRadius

        // Check if the event is within the bounds of the editBtn (it's smaller so we don't care about circular)
        val rect = Rect()
        editBtn.getHitRect(rect)
        val pressedEditBtn = rect.contains(event.x.toInt(), event.y.toInt())

        // If not in the bounds of a pressable area, ignore the event
        if (!pressedPhotoView && !pressedEditBtn) {
            return true
        }

        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        maintainRatio()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        alignEditBtn()
    }

    private fun alignEditBtn() {
        val radius = imageView.measuredWidth.toFloat() / 2f
        val len = sqrt(0.5f * radius.pow(2))

        val photoViewMiddleX = imageView.x + imageView.measuredWidth / 2f
        val photoViewMiddleY = imageView.y + imageView.measuredHeight / 2f

        val params = editBtn.layoutParams as RelativeLayout.LayoutParams
        params.leftMargin = (photoViewMiddleX + len - editBtn.measuredWidth / 2f).toInt()
        params.topMargin = (photoViewMiddleY + len - editBtn.measuredHeight / 2f).toInt()
        editBtn.layoutParams = params
    }

    private fun switchToEditMode() {
        editBtn.icon = ContextCompat.getDrawable(context, R.drawable.ic_add)
        editBtn.isClickable = false
    }

    private fun switchToClearMode() {
        editBtn.icon = ContextCompat.getDrawable(context, R.drawable.ic_clear)
        editBtn.isClickable = true
        editBtn.isDuplicateParentStateEnabled = false
    }

    private fun maintainRatio() {
        val min = imageView.measuredWidth.coerceAtMost(imageView.measuredHeight)
        imageView.layoutParams = imageView.layoutParams.apply {
            width = min
            height = min
        }
    }

    private fun makeClickable() {
        isClickable = true
        isFocusable = true

        imageView.isDuplicateParentStateEnabled = true
        editBtn.isDuplicateParentStateEnabled = true
        rippleView.isDuplicateParentStateEnabled = true
        editBtn.isClickable = false
    }
}