package com.ethanprentice.fitter.ui.user.welcome

import android.content.Context
import android.icu.text.NumberFormat
import android.icu.util.MeasureUnit
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.measure.FitMeasure
import com.ethanprentice.fitter.datamodel.data.measure.Height
import com.ethanprentice.fitter.datamodel.data.measure.Weight
import com.ethanprentice.fitter.datamodel.data.user.FitUserBuilder
import com.ethanprentice.fitter.ui.view.MeasuredEditText
import java.util.*

class WelcomeMeasurementFragment : WelcomeFragment() {

    private var fragTransitionEnded = false

    private lateinit var inputs: LinearLayout

    private lateinit var currentWeightInput : MeasuredEditText
    private lateinit var currentHeightInput : MeasuredEditText
    private lateinit var weightGoalInput : MeasuredEditText

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

        // set measure units
        // TODO: set these from preferences?  Since we aren't authenticated we should probably set these based on locale here instead though
        currentWeightInput.measureUnit = MeasureUnit.POUND
        currentHeightInput.measureUnit = MeasureUnit.CENTIMETER
        weightGoalInput.measureUnit = MeasureUnit.POUND

        // text listeners
        currentWeightInput.editText?.addTextChangedListener(
            ViewModelPusher(currentWeightInput, model::setUserCurrentWeight, ::Weight)
        )
        currentHeightInput.editText?.addTextChangedListener(
            ViewModelPusher(currentHeightInput, model::setUserCurrentHeight, ::Height)
        )
        weightGoalInput.editText?.addTextChangedListener(
            ViewModelPusher(weightGoalInput, model::setUserWeightGoal, ::Weight)
        )

        // If we have an enter animation for this frag, set singleSelects alpha to 0, to be faded in later
        if (savedInstanceState == null && hasFragSharedElemTransition && !fragTransitionEnded) {
            inputs.alpha = 0f
        }

        model.userCurrentWeight.observe(requireActivity(), Observer { currentWeight ->
            currentWeightInput.measure = currentWeight
        })
        model.userCurrentHeight.observe(requireActivity(), Observer { currentHeight ->
            currentHeightInput.measure = currentHeight
        })
        model.userWeightGoal.observe(requireActivity(), Observer { weightGoal ->
            weightGoalInput.measure = weightGoal
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

    override fun onActionClicked() {
        try {
            listener?.userReadyToBuild(model.getBuilder())
        } catch(e: FitUserBuilder.UserBuildException) {
            Toast.makeText(context, "Please make sure required fields are filled", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Parses changes from a [MeasuredEditText] to push to the [WelcomeViewModel] using [setter]
     */
    class ViewModelPusher<T : FitMeasure>(private val editText: MeasuredEditText, private val setter: (T?) -> Unit, private val factory: (Number, MeasureUnit?, Date) -> T) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if(editText.text.isNotBlank()) {
                val currTime = Calendar.getInstance().time

                val number = NumberFormat.getInstance().parse(editText.text)

                val measure = factory(number, editText.measureUnit, currTime)
                setter(measure)
            } else {
                setter(null)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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