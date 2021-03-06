package com.ethanprentice.fitter.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.util.SwimlaneItem
import com.ethanprentice.fitter.ui.FitFragment
import com.ethanprentice.fitter.ui.nav.NavActivity
import com.ethanprentice.fitter.ui.view.misc.VerticalSpaceItemDecoration
import com.ethanprentice.fitter.ui.view.swimlane.SwimlaneAdapter
import com.ethanprentice.fitter.ui.view.swimlane.SwimlaneView
import com.ethanprentice.fitter.ui.workout.WorkoutCardAdapter
import com.ethanprentice.fitter.viewmodel.HomeViewModel

class HomeFragment : FitFragment() {

    private val model: HomeViewModel by activityViewModels()

    private var exploreSwimlane: SwimlaneView? = null

    private var recentWorkoutsView: RecyclerView? = null
    private var recentWorkoutsAdapter: WorkoutCardAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreSwimlane = view.findViewById(R.id.swimlane)
        recentWorkoutsView = view.findViewById<RecyclerView>(R.id.recent_workouts).apply {
            recentWorkoutsAdapter = WorkoutCardAdapter(model.recentWorkouts.value!!).apply {
                setOnWorkoutLoggedListener {
                    (requireActivity() as NavActivity).runWorkoutQueries()
                }
            }

            adapter = recentWorkoutsAdapter

            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically() = false
                override fun isAutoMeasureEnabled() = true
            }
            setHasFixedSize(false)
            isNestedScrollingEnabled = false

            val dividerItemDecoration = VerticalSpaceItemDecoration(context.resources.getDimensionPixelOffset(R.dimen.LU_3), endingSpace=true)
            addItemDecoration(dividerItemDecoration)
        }

        model.recentWorkouts.observe(requireActivity(), Observer { workouts ->
            recentWorkoutsAdapter?.notifyDataSetChanged()
        })

        initExploreWorkouts()
    }

    override fun onResume() {
        super.onResume()
        fitActivity?.setToolbarTitle(R.string.my_home)
        recentWorkoutsAdapter?.notifyDataSetChanged()
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

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance(b: Bundle? = null): HomeFragment {
            val frag = HomeFragment()
            frag.arguments = b
            return frag
        }
    }
}