package com.portalpirates.cufit.ui.user.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.portalpirates.cufit.R

class WelcomeIntroFragment : WelcomeFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
        startPostponedEnterTransition()
    }

    override fun onActionClicked() {
        val frag = WelcomeAddPhotoFragment.newInstance()
        getWelcomeTransaction(frag, WelcomeAddPhotoFragment.TAG).commit()
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