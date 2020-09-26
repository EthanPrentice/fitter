package com.portalpirates.cufit.ui.user.welcome

import android.content.Context
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.height.Height
import com.portalpirates.cufit.datamodel.data.preferences.MeasurementUnits
import com.portalpirates.cufit.datamodel.data.weight.Weight
import com.portalpirates.cufit.ui.view.ChooseImageButton
import com.portalpirates.cufit.ui.view.FitEditText
import java.util.*

class WelcomeMeasurementFragment : WelcomeFragment() {

    private var fragTransitionEnded = false

    private lateinit var chooseImageButton: ChooseImageButton
    private lateinit var inputs: LinearLayout

    private lateinit var currentWeightInput : FitEditText
    private lateinit var currentHeightInput : FitEditText
    private lateinit var weightGoalInput : FitEditText

    private val model: WelcomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_measurement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputs = view.findViewById(R.id.inputs)
        currentWeightInput = inputs.findViewById(R.id.current_weight_input)
        currentHeightInput = inputs.findViewById(R.id.current_height_input)
        weightGoalInput = inputs.findViewById(R.id.weight_goal_input)

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

        model.userCurrentWeight.observe(requireActivity(), Observer { currentWeight ->
            currentWeightInput.text = (currentWeight?.mass ?: "").toString()
        })
        model.userCurrentHeight.observe(requireActivity(), Observer { currentHeight ->
            currentHeightInput.text = (currentHeight?.length ?: "").toString()
        })
        model.userWeightGoal.observe(requireActivity(), Observer { weightGoal ->
            weightGoalInput.text = (weightGoal?.mass ?: "").toString()
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
        // If the fragment is paused and the transition has no finished, show the views
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

        ifEmptyThenNull(currentWeightInput.text)?.let {
            val userCurrentWeight = Weight(it.toDouble(), MeasurementUnits.KILOGRAMS, Calendar.getInstance().time)
            model.setUserCurrentWeight(userCurrentWeight)
        }
        ifEmptyThenNull(currentHeightInput.text)?.let {
            val userCurrentHeight = Height(it.toDouble(), MeasurementUnits.METERS, Calendar.getInstance().time)
            model.setUserCurrentHeight(userCurrentHeight)
        }
        ifEmptyThenNull(weightGoalInput.text)?.let {
            val userWeightGoal = Weight(it.toDouble(), MeasurementUnits.KILOGRAMS, Calendar.getInstance().time)
            model.setUserWeightGoal(userWeightGoal)
        }
    }

    private fun toNextFrag() {
        pushToViewModel()

    }

    companion object {
        const val TAG = "WelcomeMeasurementFragment"

        fun newInstance(bundle: Bundle? = null): WelcomeMeasurementFragment {
            val frag = WelcomeMeasurementFragment()
            frag.arguments = bundle
            return frag
        }
    }

}