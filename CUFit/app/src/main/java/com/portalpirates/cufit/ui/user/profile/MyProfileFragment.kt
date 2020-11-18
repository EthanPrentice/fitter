package com.portalpirates.cufit.ui.user.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.appbar.AppBarLayout
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.user.profile.view.MyProfileCardView
import com.portalpirates.cufit.ui.user.profile.view.RecentWorkoutsCardView
import com.portalpirates.cufit.ui.view.chart.LineChartCardView
import kotlin.math.abs

class MyProfileFragment : FitFragment(), AppBarLayout.OnOffsetChangedListener {

    var user: AuthenticatedUser? = null

    var myProfileCard: MyProfileCardView? = null
    var recentWorkoutsCard: RecentWorkoutsCardView? = null
    var progressCard: LineChartCardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FitApplication.instance.userManager.provider.getAuthenticatedUser(object :
            TaskListener<AuthenticatedUser?> {
            override fun onSuccess(value: AuthenticatedUser?) {
                user = value
                if (value != null) {
                    myProfileCard?.setUser(value)
                }
            }

            override fun onFailure(e: Exception?) {
                e?.message?.let {
                    Log.e(TAG, it)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fitActivity?.appBar?.removeOnOffsetChangedListener(this)
        fitActivity?.setToolbarTitle(R.string.my_profile)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.my_profile_layout, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myProfileCard = view.findViewById(R.id.my_profile_card)
        user?.let {
            myProfileCard?.setUser(it)
        }

        recentWorkoutsCard = view.findViewById(R.id.recent_workouts_card)
        initRecentWorkoutsCard()

        progressCard = view.findViewById(R.id.progress_chart_test)
        initProgressCard()

    }

    private fun initProgressCard() {
        val lineData = FitApplication.instance.userManager.provider.getBodyWeightLineDataSet()
        progressCard?.apply {
            setData(lineData)
            setTitle("Progress")
            isIncreaseGood = false // TODO: read this from user preferences when programmed
        }
    }

    private fun initRecentWorkoutsCard() {

        class FakeWorkout(private val title: String, private val bmp: Bitmap?) : SwimlaneItem {
            override fun getTitle(): String = title
            override fun getDrawable(): Drawable? {
                return if (bmp == null) {
                    null
                } else {
                    BitmapDrawable(resources, bmp)
                }
            }
        }

        recentWorkoutsCard?.let { card ->
            val titles = listOf("Friday", "Wednesday", "Monday", "Oct 30")
            val bitmapResIds = listOf(R.raw.bench_lady, R.raw.jogging, R.raw.back, R.raw.jogging)

            val swimlaneItems = List<SwimlaneItem>(titles.size) { i ->
                val bmp = if (bitmapResIds[i] == 0) {
                    null
                } else {
                    BitmapFactory.decodeResource(resources, bitmapResIds[i])
                }
                FakeWorkout(titles[i], bmp)
            }

            card.setSwimlaneItems(swimlaneItems)
        }

//        val recentWorkouts = FitApplication.instance.userManager.provider.getRecentWorkouts()
//        recentWorkoutsCard?.setSwimlaneItems(recentWorkouts)
        recentWorkoutsCard?.setOnItemClickListener { v, item ->
            Toast.makeText(context, "${item.getTitle()} was pressed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        fitActivity?.appBar?.removeOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (appBarLayout == null) {
            return
        }

        val collapsedToolbar = fitActivity?.collapsingToolbar
        if (collapsedToolbar != null) {
            val percentCollapsed = abs(verticalOffset / appBarLayout.totalScrollRange.toFloat())

            if (percentCollapsed >= 0.5f && collapsedToolbar.title != user?.fullName) {
                collapsedToolbar.title = user?.fullName
            }
            else if (percentCollapsed <= 0.5f && collapsedToolbar.title != resources.getString(R.string.my_profile)) {
                collapsedToolbar.title = resources.getString(R.string.my_profile)
            }
        }
    }

    companion object {
        const val TAG = "MyProfileFragment"

        fun newInstance(b: Bundle? = null): MyProfileFragment {
            val frag = MyProfileFragment()
            frag.arguments = b
            return frag
        }
    }

}