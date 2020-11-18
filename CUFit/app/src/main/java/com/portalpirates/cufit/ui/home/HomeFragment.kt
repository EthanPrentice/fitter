package com.portalpirates.cufit.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.cloud.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneAdapter
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneView
import com.portalpirates.cufit.ui.workout.view.WorkoutCardView

class HomeFragment : FitFragment() {

    private var exploreSwimlane: SwimlaneView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreSwimlane = view.findViewById(R.id.swimlane)
        initExploreWorkouts()

        doFakeDataStuff(view)
    }

    override fun onResume() {
        super.onResume()
        fitActivity?.setToolbarTitle(R.string.my_home)
    }

    private fun initExploreWorkouts() {
        fun onExploreItemClick(v: View?, item: SwimlaneItem) {
            // Toast.makeText(requireContext(), "${item.getTitle()} was clicked", Toast.LENGTH_SHORT).show()
        }

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

        exploreSwimlane?.let { swimlane ->
//            val swimlaneItems = FitApplication.instance.workoutManager.provider.getExploreWorkouts()

            val titles = listOf("Toned Chest & Triceps", "nSuns' Back Day", "Ultimate Full Body", "Cardio")
            val bitmapResIds = listOf(R.raw.bench_lady, R.raw.back, R.raw.kettle_bell_lady, R.raw.jogging)

            val swimlaneItems = List<SwimlaneItem>(titles.size) { i ->
                val bmp = if (bitmapResIds[i] == 0) {
                    null
                } else {
                    BitmapFactory.decodeResource(resources, bitmapResIds[i])
                }
                FakeWorkout(titles[i], bmp)
            }

            val swimlaneAdapter = SwimlaneAdapter(requireContext(), swimlaneItems, ContextCompat.getDrawable(requireContext(), R.drawable.default_workout_img)!!, ::onExploreItemClick)
            swimlane.adapter = swimlaneAdapter
        }
    }

    private fun doFakeDataStuff(view: View) {
        val workout1 = view.findViewById<WorkoutCardView>(R.id.fake_workout_view1).apply {
            setTitle("Chest & Tris")
            setFakeDescription("Bench press, pec flies, squeeze press, tricep extensions, skullcrushers")
            setFakeLabels(listOf("Chest", "Triceps", "Shoulders"))
        }

        view.findViewById<WorkoutCardView>(R.id.fake_workout_view2).apply {
            setTitle("Back & Biceps")
            setFakeDescription("Lat pulldowns, seated row, straight arm pulldowns, incline curls, hammer curls")
            setFakeLabels(listOf("Back", "Biceps", "Shoulders"))
        }

        val workout3 = view.findViewById<WorkoutCardView>(R.id.fake_workout_view3).apply {
            setTitle("Legs & Shoulders")
            setFakeDescription("Squats, calf raises, hamstring curls, leg extensions, overhead press, delt flies, front raises, side raises")
            setFakeLabels(listOf("Legs", "Shoulders"))
        }

        FitApplication.instance.userManager.provider.getAuthenticatedUser(object : TaskListener<AuthenticatedUser?> {
            override fun onSuccess(value: AuthenticatedUser?) {
                workout1.setFakeOwnerBmp(value?.imageBmp)
                workout3.setFakeOwnerBmp(value?.imageBmp)
            }

            override fun onFailure(e: Exception?) { }
        })
    }

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance(b: Bundle? = null): HomeFragment {
            val frag = HomeFragment()
            frag.arguments = b
            return frag
        }
    }
}