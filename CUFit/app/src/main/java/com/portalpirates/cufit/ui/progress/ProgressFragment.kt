package com.portalpirates.cufit.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitFragment

class ProgressFragment : FitFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.progress_frag_layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        fitActivity?.setToolbarTitle(R.string.my_progress)
    }

    companion object {
        const val TAG = "ProgressFragment"

        fun newInstance(b: Bundle? = null): ProgressFragment {
            val frag = ProgressFragment()
            frag.arguments = b
            return frag
        }
    }
}