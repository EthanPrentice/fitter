package com.portalpirates.cufit.ui.user.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.view.ChooseImageButton

class WelcomeAddPhotoFragment : WelcomeFragment() {

    private lateinit var chooseImageButton: ChooseImageButton

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


        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
        startPostponedEnterTransition()
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