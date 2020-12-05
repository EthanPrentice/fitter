package com.portalpirates.cufit.ui.user.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.appbar.AppBarLayout
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.datamodel.data.workout.Workout
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.home.HomeViewModel
import com.portalpirates.cufit.ui.nav.NavActivity
import com.portalpirates.cufit.ui.user.profile.view.MyProfileCardView
import com.portalpirates.cufit.ui.view.chart.LineChartCardView
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneCardView
import com.portalpirates.cufit.ui.workout.view.CreateWorkoutCardView
import com.portalpirates.cufit.ui.workout.view.WorkoutCardView
import kotlin.math.abs

class MyProfileFragment : FitFragment(), AppBarLayout.OnOffsetChangedListener {

    private val model: HomeViewModel by activityViewModels()

    var user: AuthenticatedUser? = null

    var myProfileCard: MyProfileCardView? = null
    var myWorkoutsCard: SwimlaneCardView? = null

    var createWorkoutCard: CreateWorkoutCardView? = null
    var currWorkoutCard: WorkoutCardView? = null

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

        myWorkoutsCard = view.findViewById(R.id.my_workouts_card)
        initMyWorkoutsCard()

        createWorkoutCard = view.findViewById(R.id.create_workout_card)
        initCreateWorkoutCard()

        progressCard = view.findViewById(R.id.progress_chart_test)
        initProgressCard()

        model.ownedWorkouts.observe(requireActivity(), Observer {
            myWorkoutsCard?.adapter?.notifyDataSetChanged()
        })

        model.recentWorkouts.observe(requireActivity(), Observer {
            initProgressCard()
        })

        // Override the chevron collapsing to close the card and when shown always be expanded
        currWorkoutCard = view.findViewById<WorkoutCardView>(R.id.curr_workout_card).apply {
            expand()
            findViewById<ImageView>(R.id.workout_chevron)?.setOnClickListener {
                visibility = View.GONE
            }
            setOnWorkoutLoggedListener {
                (requireActivity() as NavActivity).runWorkoutQueries()
            }
        }
    }

    private fun initProgressCard() {
//        val lineData = FitApplication.instance.userManager.provider.getBodyWeightLineDataSet()
        progressCard?.setTitle("Progress")

        var firstExercise: Exercise? = null
        model.recentWorkouts.value?.forEach { workout ->
            if (workout.exercises.isNotEmpty()) {
                firstExercise = workout.exercises[0]
            }
        }

        firstExercise?.let {
            FitApplication.instance.workoutManager.provider.getExerciseDataSet(
                FitApplication.instance.userManager.provider.getFirebaseUser()!!.uid,
                it.name,
                null,
                object : TaskListener<LineDataSet> {
                    override fun onSuccess(value: LineDataSet) {
                        progressCard?.apply {
                            setData(value)
                            isIncreaseGood = true // TODO: read this from user preferences when programmed
                        }
                    }

                    override fun onFailure(e: Exception?) {
                        Log.w(TAG, "Could not get progress!")
                    }
                }
            )
        }

    }

    private fun initMyWorkoutsCard() {
        myWorkoutsCard?.let { card ->
            card.setSwimlaneItems(model.ownedWorkouts.value!!)
            card.setTitle("My Workouts")
            card.setStatusText("2 week streak")
        }

        myWorkoutsCard?.adapter?.enableAddSwimlaneItem {
            currWorkoutCard?.visibility = View.GONE
            createWorkoutCard?.visibility = View.VISIBLE
            null
        }

        myWorkoutsCard?.setOnItemClickListener { v, item ->
            currWorkoutCard?.setWorkout(item as Workout)
            currWorkoutCard?.visibility = View.VISIBLE
            createWorkoutCard?.visibility = View.GONE
            null
        }
    }

    private fun initCreateWorkoutCard() {
        createWorkoutCard?.apply {
            assignLock(model.imageSelectorLock)
            populateMuscleGroups(model.muscleGroups)
            setOnCreateTaskListener(object : TaskListener<Workout> {
                override fun onSuccess(value: Workout) {
                    Toast.makeText(context, "Workout created!", Toast.LENGTH_LONG).show()
                    model.insertOwnedWorkout(0, value)

                    createWorkoutCard?.apply {
                        visibility = View.GONE
                        clearData()
                    }
                }

                override fun onFailure(e: Exception?) {
                    Log.w("Create Workout", "There was an error creating the workout!")
                    Toast.makeText(context, "There was an error creating the workout!", Toast.LENGTH_LONG).show()
                }
            })

            setOnCancelListener {
                visibility = View.GONE
                clearData()
            }
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