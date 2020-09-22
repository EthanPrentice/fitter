package com.portalpirates.cufit.ui.user.welcome

import android.content.Context
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.view.ChooseImageButton
import com.portalpirates.cufit.ui.view.adapter.SingleSelectAdapter


class WelcomeSelectSexFragment : WelcomeFragment() {

    private var fragTransitionEnded = false

    private lateinit var chooseImageButton: ChooseImageButton
    private lateinit var singleSelects: RecyclerView

    private val model: WelcomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_layout3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        singleSelects = view.findViewById(R.id.single_select)
        chooseImageButton = view.findViewById(R.id.choose_photo_btn)

        // If we have an enter animation for this frag, set singleSelects alpha to 0, to be faded in later
        if (savedInstanceState == null && hasFragSharedElemTransition && !fragTransitionEnded) {
            singleSelects.alpha = 0f
        }

        chooseImageButton.setOnClickListener {
            chooseImageButton.selectPhotoFromGallery(requireActivity())
        }

        // Disable scrolling - views will always be fully visible
        singleSelects.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically() = false
        }

        // If there's already a chosen sex for the user and we recreate this frag, use what's in the view model as default
        var defaultSex: String? = null
        model.userSex.observe(requireActivity(), Observer { str ->
            if (str != null) {
                defaultSex = str
            }
        })

        singleSelects.adapter = SingleSelectAdapter(requireContext(), R.array.sexes, defaultSex).apply {
            setOnCheckedChangeListener {
                model.setUserSex(getChecked())
            }
        }

        model.userImage.observe(requireActivity(), Observer { bmp ->
            if (bmp != null) {
                chooseImageButton.setImageBitmap(bmp)
            }
        })

        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
        startPostponedEnterTransition()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // fade views in once the sharedElements are done being animated
        val sharedElementEnterTransition = sharedElementEnterTransition as? Transition?
        sharedElementEnterTransition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition) { }
            override fun onTransitionPause(transition: Transition) { }
            override fun onTransitionStart(transition: Transition) {
                fun View.fadeIn() = animate().alpha(1f).setDuration(FRAG_TRANSITION_MS).start()
                singleSelects.fadeIn()
            }

            override fun onTransitionEnd(transition: Transition) {
                fragTransitionEnded = true
            }
            override fun onTransitionCancel(transition: Transition) = onTransitionEnd(transition)
        })
    }

    override fun onPause() {
        super.onPause()

        // Transition ends and does not update the listener on-pause
        // If the fragment is paused and the transition has not finished, show the views
        if (hasActivitySharedElemTransition && !fragTransitionEnded) {
            singleSelects.alpha = 1f
        }
    }


    companion object {
        const val TAG = "WelcomeAddPhotoFragment"

        fun newInstance(bundle: Bundle? = null): WelcomeSelectSexFragment {
            val frag = WelcomeSelectSexFragment()
            frag.arguments = bundle
            return frag
        }
    }

}