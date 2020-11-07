package com.portalpirates.cufit.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneAdapter
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneView

class HomeFragment : FitFragment() {

    private var exploreSwimlane: SwimlaneView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreSwimlane = view.findViewById(R.id.swimlane)
        initExploreWorkouts()
    }

    override fun onResume() {
        super.onResume()
        fitActivity?.setToolbarTitle(R.string.my_home)
    }

    fun initExploreWorkouts() {
        fun onExploreItemClick(v: View?, item: SwimlaneItem) {
            Toast.makeText(requireContext(), "${item.getTitle()} was clicked", Toast.LENGTH_SHORT).show()
        }

        exploreSwimlane?.let { swimlane ->
            val swimlaneItems = FitApplication.instance.workoutManager.provider.getExploreWorkouts()
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