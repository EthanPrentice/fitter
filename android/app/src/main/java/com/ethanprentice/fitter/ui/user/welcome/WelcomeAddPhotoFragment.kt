package com.ethanprentice.fitter.ui.user.welcome

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.ethanprentice.fitter.R

class WelcomeAddPhotoFragment : WelcomeFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
        startPostponedEnterTransition()
    }

    override fun onActionClicked() {
        val b = Bundle()
        b.putBoolean(HAS_FRAG_SHARED_ELEM_TRANSITION, true)
        val frag = WelcomePersonalFragment.newInstance(b)
        getWelcomeTransaction(frag, WelcomePersonalFragment.TAG).commit()
    }

    private fun toNextFrag() {
        val b = Bundle()
        b.putBoolean(HAS_FRAG_SHARED_ELEM_TRANSITION, true)
        val frag = WelcomePersonalFragment.newInstance(b)

        val transition = AutoTransition().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            duration = FRAG_TRANSITION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }

        frag.sharedElementEnterTransition = transition
        frag.sharedElementReturnTransition = transition

//        requireActivity().supportFragmentManager
//            .beginTransaction()
//            .setReorderingAllowed(true)
//            .addSharedElement(actionBtn, resources.getString(R.string.tr_action_btn))
//            .addSharedElement(chooseImageButton, resources.getString(R.string.tr_choose_photo_btn))
//            .addSharedElement(chooseImageButton.imageView, chooseImageButton.imageView.transitionName)
//            .addSharedElement(chooseImageButton.editBtn, chooseImageButton.editBtn.transitionName)
//            .replace(R.id.frag_container, frag, WelcomePersonalFragment.TAG)
//            .addToBackStack(null)
//            .commit()
    }

    companion object {
        const val TAG = "WelcomeAddPhotoFragment"

        fun newInstance(bundle: Bundle? = null): WelcomeAddPhotoFragment {
            val frag = WelcomeAddPhotoFragment()
            frag.arguments = bundle
            return frag
        }
    }

}