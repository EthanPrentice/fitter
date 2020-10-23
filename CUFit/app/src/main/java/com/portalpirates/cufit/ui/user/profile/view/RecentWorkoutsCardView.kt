package com.portalpirates.cufit.ui.user.profile.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.view.FitCardView
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneAdapter
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneView

class RecentWorkoutsCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        // TODO: When workouts are done in data model, don't use mock data
        class SwimlaneItemTest(private val title: String) : SwimlaneItem {
            override fun getTitle(): String {
                return title
            }
            override fun getDrawable(): Drawable? {
                return null
            }
        }

        val swimlaneItems = ArrayList<SwimlaneItem>().apply {
            add(SwimlaneItemTest("Test 1"))
            add(SwimlaneItemTest("Test 2"))
            add(SwimlaneItemTest("Test 3"))
            add(SwimlaneItemTest("Test 4"))
            add(SwimlaneItemTest("Test 5"))
            add(SwimlaneItemTest("Test 6"))
            add(SwimlaneItemTest("Test 7"))
        }

        fun onClickTest(v: View?, item: SwimlaneItem) {
            Toast.makeText(context, "${item.getTitle()} was clicked", Toast.LENGTH_SHORT).show()
        }

        val swimlaneAdapter = SwimlaneAdapter(context, swimlaneItems, ContextCompat.getDrawable(context, R.drawable.default_avatar)!!, ::onClickTest)
        val swimlane = SwimlaneView(context)
        swimlane.adapter = swimlaneAdapter
        swimlane.visibleItems = 4f
        swimlane.setTextAppearance(R.style.subtitle)

        setContentView(swimlane, (160*1.5f).toInt())
        topBarVisible = true
        setTitle("Workouts")
    }

}