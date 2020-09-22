package com.portalpirates.cufit.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.portalpirates.cufit.FitActivity

abstract class FitFragment : Fragment() {

    protected val fitActivity: FitActivity?
        get() = activity as? FitActivity

    protected var hasActivitySharedElemTransition = false
        private set

    protected var hasFragSharedElemTransition = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            hasActivitySharedElemTransition = args.getBoolean(FitActivity.HAS_ACTIVITY_SHARED_ELEM_TRANSITION, false)
            hasFragSharedElemTransition = savedInstanceState == null && args.getBoolean(HAS_FRAG_SHARED_ELEM_TRANSITION, false)
        }
    }

    companion object {
        const val FRAG_TRANSITION_MS = 300L

        const val HAS_FRAG_SHARED_ELEM_TRANSITION = "com.portalpirates.cufit.ui.FitFragment:hasFragmentSharedElemTransition"
    }
}