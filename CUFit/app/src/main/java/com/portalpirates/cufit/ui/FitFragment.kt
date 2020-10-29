package com.portalpirates.cufit.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class FitFragment : Fragment() {

    protected val fitActivity: FitActivity?
        get() = activity as? FitActivity

    protected var hasActivitySharedElemTransition = false
        private set

    protected var hasFragSharedElemTransition = false
        private set

    var rootView: View? = null
        protected set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            hasActivitySharedElemTransition = savedInstanceState == null && args.getBoolean(
                FitActivity.HAS_ACTIVITY_SHARED_ELEM_TRANSITION, false)
            hasFragSharedElemTransition = savedInstanceState == null && args.getBoolean(HAS_FRAG_SHARED_ELEM_TRANSITION, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
    }

    companion object {
        const val FRAG_TRANSITION_MS = 300L

        const val HAS_FRAG_SHARED_ELEM_TRANSITION = "com.portalpirates.cufit.ui.FitFragment:hasFragmentSharedElemTransition"
    }
}