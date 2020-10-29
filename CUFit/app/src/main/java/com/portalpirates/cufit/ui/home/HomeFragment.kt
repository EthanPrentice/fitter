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
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneAdapter
import com.portalpirates.cufit.ui.view.swimlane.SwimlaneView

class HomeFragment : FitFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            Toast.makeText(requireContext(), "${item.getTitle()} was clicked", Toast.LENGTH_SHORT).show()
        }

        val swimlaneAdapter = SwimlaneAdapter(requireContext(), swimlaneItems, ContextCompat.getDrawable(requireContext(), R.drawable.default_avatar)!!, ::onClickTest)
        val swimlane = view.findViewById<SwimlaneView>(R.id.swimlane)
        swimlane.adapter = swimlaneAdapter
    }

    override fun onResume() {
        super.onResume()
        fitActivity?.setToolbarTitle(R.string.my_home)
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