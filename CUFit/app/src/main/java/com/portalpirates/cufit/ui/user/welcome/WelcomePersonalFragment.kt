package com.portalpirates.cufit.ui.user.welcome

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.ChooseImageButton
import com.portalpirates.cufit.ui.view.FitDatePicker
import com.portalpirates.cufit.ui.view.FitEditText

class WelcomePersonalFragment : WelcomeFragment() {

    private var fragTransitionEnded = false

    private lateinit var chooseImageButton: ChooseImageButton
    private lateinit var inputs: LinearLayout

    private lateinit var firstNameInput: FitEditText
    private lateinit var lastNameInput: FitEditText
    private lateinit var birthDateInput: FitDatePicker

    private val model: WelcomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputs = view.findViewById(R.id.inputs)
        firstNameInput = inputs.findViewById(R.id.first_name_input)
        lastNameInput = inputs.findViewById(R.id.last_name_input)
        birthDateInput = inputs.findViewById(R.id.birth_date_input)

        chooseImageButton = view.findViewById(R.id.choose_photo_btn)

        // If we have an enter animation for this frag, set singleSelects alpha to 0, to be faded in later
        if (savedInstanceState == null && hasFragSharedElemTransition && !fragTransitionEnded) {
            inputs.alpha = 0f
        }

        chooseImageButton.setOnClickListener {
            chooseImageButton.selectPhotoFromGallery(requireActivity())
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

        model.userFirstName.observe(requireActivity(), Observer { firstName ->
            firstNameInput.text = firstName ?: ""
        })
        model.userLastName.observe(requireActivity(), Observer { lastName ->
            lastNameInput.text = lastName ?: ""
        })
        model.userBirthDate.observe(requireActivity(), Observer { birthDate ->
            birthDateInput.date = birthDate
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
                inputs.fadeIn()
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
            inputs.alpha = 1f
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pushToViewModel()
    }

    override fun onDestroy() {
        pushToViewModel()
        super.onDestroy()
    }

    private fun pushToViewModel() {
        fun ifEmptyThenNull(str: String?): String? {
            return if (str.isNullOrBlank()) {
                null
            } else {
                str.toString()
            }
        }

        ifEmptyThenNull(firstNameInput.text)?.let {
            model.setUserFirstName(it)
        }
        ifEmptyThenNull(lastNameInput.text)?.let {
            model.setUserLastName(it)
        }
        birthDateInput.date?.let {
            model.setUserBirthDate(it)
        }
    }

    private fun toNextFrag() {
        pushToViewModel()

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

        val transaction = requireActivity().supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(actionBtn, resources.getString(R.string.tr_action_btn))
            .addSharedElement(chooseImageButton, resources.getString(R.string.tr_choose_photo_btn))
            .addSharedElement(chooseImageButton.imageView, chooseImageButton.imageView.transitionName)
            .addSharedElement(chooseImageButton.editBtn, chooseImageButton.editBtn.transitionName)
            .replace(R.id.frag_container, frag, WelcomeSelectSexFragment.TAG)
            .addToBackStack(null)

        // Fade out inputs then start transaction
        inputs.animate().alpha(0f).setDuration(FRAG_TRANSITION_MS).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) { }
            override fun onAnimationRepeat(animation: Animator?) { }
            override fun onAnimationEnd(animation: Animator?) {
                transaction.commit()
            }
            override fun onAnimationCancel(animation: Animator?) = onAnimationEnd(animation)
        }).start()
    }


    companion object {
        const val TAG = "WelcomePersonalFragment"

        fun newInstance(bundle: Bundle? = null): WelcomePersonalFragment {
            val frag = WelcomePersonalFragment()
            frag.arguments = bundle
            return frag
        }
    }
}