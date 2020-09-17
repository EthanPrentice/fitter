package com.portalpirates.cufit.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.portalpirates.cufit.FitActivity

abstract class FitFragment : Fragment() {

    protected val fitActivity: FitActivity?
        get() = activity as? FitActivity

    protected var hasActivitySharedElemTransition = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            hasActivitySharedElemTransition = args.getBoolean(FitActivity.HAS_ACTIVITY_SHARED_ELEM_TRANSITION, false)
        }
    }

    companion object {
        const val FRAG_TRANSITION_MS = 300L
    }
}