package com.portalpirates.cufit.ui.user.welcome

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.user.auth.AuthFragment
import com.portalpirates.cufit.ui.view.FitButton

class WelcomeIntroFragment : WelcomeFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_layout1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }

        actionBtn = view.findViewById(R.id.action_btn)
        topText = view.findViewById(R.id.top_text)

        actionBtn.setOnClickListener {
            toNextFrag()
        }

        startPostponedEnterTransition()
    }


    fun toNextFrag() {
        val frag = WelcomeAddPhotoFragment.newInstance()

        val transition = TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeClipBounds())
            addTransition(ChangeTransform())
            addTransition(ChangeImageTransform())

            ordering = TransitionSet.ORDERING_TOGETHER
            duration = FRAG_TRANSITION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }

        frag.sharedElementEnterTransition = transition
        frag.sharedElementReturnTransition = transition

        requireActivity().supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(topText!!, resources.getString(R.string.tr_top_text))
            .addSharedElement(actionBtn, resources.getString(R.string.tr_action_btn))
            .replace(R.id.frag_container, frag, WelcomeAddPhotoFragment.TAG)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        const val TAG = "WelcomeIntroFragment"

        fun newInstance(bundle: Bundle? = null): WelcomeIntroFragment {
            val frag = WelcomeIntroFragment()
            frag.arguments = bundle
            return frag
        }
    }

}