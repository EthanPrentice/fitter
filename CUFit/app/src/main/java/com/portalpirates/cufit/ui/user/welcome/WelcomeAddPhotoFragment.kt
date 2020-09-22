package com.portalpirates.cufit.ui.user.welcome

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.ChooseImageButton

class WelcomeAddPhotoFragment : WelcomeFragment() {

    private lateinit var chooseImageButton: ChooseImageButton

    private val model: WelcomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_layout2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chooseImageButton = view.findViewById(R.id.choose_photo_btn)
        chooseImageButton.setOnClickListener {
            chooseImageButton.selectPhotoFromGallery(requireActivity())
        }
        chooseImageButton.setOnClearListener { v ->
            model.setUserImage(null)
        }

        model.userImage.observe(requireActivity(), Observer { bmp ->
            if (bmp != null) {
                chooseImageButton.setImageBitmap(bmp)
            }
        })

        actionBtn = view.findViewById(R.id.action_btn)

        actionBtn.setOnClickListener {
            toNextFrag()
        }

        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
        startPostponedEnterTransition()
    }

    private fun toNextFrag() {
        val b = Bundle()
        b.putBoolean(HAS_FRAG_SHARED_ELEM_TRANSITION, true)
        val frag = WelcomeSelectSexFragment.newInstance(b)

        val transition = AutoTransition().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            duration = FRAG_TRANSITION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }

        frag.sharedElementEnterTransition = transition
        frag.sharedElementReturnTransition = transition

        requireActivity().supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(actionBtn, resources.getString(R.string.tr_action_btn))
            .addSharedElement(chooseImageButton, resources.getString(R.string.tr_choose_photo_btn))
            .addSharedElement(chooseImageButton.imageView, chooseImageButton.imageView.transitionName)
            .addSharedElement(chooseImageButton.editBtn, chooseImageButton.editBtn.transitionName)
            .replace(R.id.frag_container, frag, WelcomeSelectSexFragment.TAG)
            .addToBackStack(null)
            .commit()
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